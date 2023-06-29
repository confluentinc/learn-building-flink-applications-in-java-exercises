package userstatistics;

import models.FlightData;
import models.TestHelpers;
import models.UserStatistics;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.junit5.MiniClusterExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserStatisticsJobTest {

    StreamExecutionEnvironment env;
    WatermarkStrategy<FlightData> defaultWatermarkStrategy;

    DataStream.Collector<UserStatistics> collector;

    static final MiniClusterResourceConfiguration miniClusterConfig = new MiniClusterResourceConfiguration.Builder()
            .setNumberSlotsPerTaskManager(2)
            .setNumberTaskManagers(1)
            .build();

    @RegisterExtension
    static final MiniClusterExtension FLINK = new MiniClusterExtension(miniClusterConfig);

    private void assertContains(DataStream.Collector<UserStatistics> collector, List<UserStatistics> expected) {
        List<UserStatistics> actual = new ArrayList<>();
        collector.getOutput().forEachRemaining(actual::add);

        assertEquals(expected.size(), actual.size());

        assertTrue(actual.containsAll(expected));
    }

    @BeforeEach
    public void setup() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        defaultWatermarkStrategy = WatermarkStrategy
                .<FlightData>forMonotonousTimestamps()
                .withTimestampAssigner((event, timestamp) -> System.currentTimeMillis());

        collector = new DataStream.Collector<>();
    }

    @Test
    public void defineWorkflow_shouldConvertFlightDataToUserStatistics() throws Exception {
        FlightData flight = new TestHelpers.FlightDataBuilder().build();

        DataStream<FlightData> stream = env
                .fromElements(flight)
                .assignTimestampsAndWatermarks(defaultWatermarkStrategy);

        UserStatisticsJob
                .defineWorkflow(stream)
                .collectAsync(collector);

        env.executeAsync();

        UserStatistics expected = new UserStatistics(flight);

        assertContains(collector, Arrays.asList(expected));
    }

    @Test
    public void defineWorkflow_shouldGroupStatisticsByEmailAddress() throws Exception {
        String email1 = TestHelpers.generateEmail();
        String email2 = TestHelpers.generateEmail();

        FlightData flight1 = new TestHelpers.FlightDataBuilder().setEmailAddress(email1).build();
        FlightData flight2 = new TestHelpers.FlightDataBuilder().setEmailAddress(email2).build();
        FlightData flight3 = new TestHelpers.FlightDataBuilder().setEmailAddress(email1).build();

        DataStream<FlightData> stream = env
                .fromElements(flight1, flight2, flight3)
                .assignTimestampsAndWatermarks(defaultWatermarkStrategy);

        UserStatisticsJob
                .defineWorkflow(stream)
                .collectAsync(collector);

        env.executeAsync();

        UserStatistics expected1 = new UserStatistics(flight1).merge(new UserStatistics(flight3));
        UserStatistics expected2 = new UserStatistics(flight2);

        assertContains(collector, Arrays.asList(expected1, expected2));
    }

    @Test
    public void defineWorkflow_shouldWindowStatisticsByMinute() throws Exception {
        String email = TestHelpers.generateEmail();
        FlightData flight1 = new TestHelpers.FlightDataBuilder().setEmailAddress(email).build();
        FlightData flight2 = new TestHelpers.FlightDataBuilder().setEmailAddress(email).build();
        FlightData flight3 = new TestHelpers.FlightDataBuilder().setEmailAddress(email).setDepartureAirportCode("LATE").build();

        WatermarkStrategy<FlightData> watermarkStrategy = WatermarkStrategy
                .<FlightData>forMonotonousTimestamps()
                .withTimestampAssigner((event, timestamp) -> {
                    if(event.getDepartureAirportCode().equals("LATE")) {
                        return System.currentTimeMillis() + Duration.ofMinutes(1).toMillis();
                    } else {
                        return System.currentTimeMillis();
                    }
                });

        DataStream<FlightData> stream = env
                .fromElements(flight1, flight2, flight3)
                .assignTimestampsAndWatermarks(watermarkStrategy);

        UserStatisticsJob
                .defineWorkflow(stream)
                .collectAsync(collector);

        env.executeAsync();

        UserStatistics expected1 = new UserStatistics(flight1).merge(new UserStatistics(flight2));
        UserStatistics expected2 = new UserStatistics(flight3);

        assertContains(collector, Arrays.asList(expected1, expected2));
    }
}