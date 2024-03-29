package flightimporter;

import models.FlightData;
import models.SkyOneAirlinesFlightData;
import models.TestHelpers;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.junit5.MiniClusterExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightImporterJobTest {

    StreamExecutionEnvironment env;
    DataStream.Collector<FlightData> collector;

    static final MiniClusterResourceConfiguration miniClusterConfig = new MiniClusterResourceConfiguration.Builder()
            .setNumberSlotsPerTaskManager(2)
            .setNumberTaskManagers(1)
            .build();

    @RegisterExtension
    static final MiniClusterExtension FLINK = new MiniClusterExtension(miniClusterConfig);

    private void assertContains(DataStream.Collector<FlightData> collector, List<FlightData> expected) {
        List<FlightData> actual = new ArrayList<>();
        collector.getOutput().forEachRemaining(actual::add);

        assertEquals(expected.size(), actual.size());

        assertTrue(actual.containsAll(expected));
    }

    @BeforeEach
    public void setup() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        collector = new DataStream.Collector<>();
    }

    @Test
    public void defineWorkflow_shouldConvertDataFromOneStreams() throws Exception {
        SkyOneAirlinesFlightData skyOneFlight = new TestHelpers.SkyOneBuilder().build();

        DataStreamSource<SkyOneAirlinesFlightData> skyOneStream = env.fromElements(skyOneFlight);

        FlightImporterJob
                .defineWorkflow(skyOneStream)
                .collectAsync(collector);

        env.executeAsync();

        assertContains(collector, Arrays.asList(skyOneFlight.toFlightData()));
    }

    @Test
    public void defineWorkflow_shouldFilterOutFlightsInThePast() throws Exception {
        SkyOneAirlinesFlightData newSkyOneFlight = new TestHelpers.SkyOneBuilder()
                .setFlightArrivalTime(ZonedDateTime.now().plusMinutes(1))
                .build();
        SkyOneAirlinesFlightData oldSkyOneFlight = new TestHelpers.SkyOneBuilder()
                .setFlightArrivalTime(ZonedDateTime.now().minusSeconds(1))
                .build();

        DataStreamSource<SkyOneAirlinesFlightData> skyOneStream = env.fromElements(newSkyOneFlight, oldSkyOneFlight);

        FlightImporterJob
                .defineWorkflow(skyOneStream)
                .collectAsync(collector);

        env.executeAsync();

        assertContains(collector, Arrays.asList(newSkyOneFlight.toFlightData()));
    }
}