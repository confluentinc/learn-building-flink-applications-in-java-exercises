package userstatistics;

import datagen.DataGeneratorJob;
import models.FlightData;
import models.UserStatistics;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.formats.json.JsonSerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.InputStream;
import java.util.Properties;

public class UserStatisticsJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties consumerConfig = new Properties();
        try (InputStream stream = DataGeneratorJob.class.getClassLoader().getResourceAsStream("consumer.properties")) {
            consumerConfig.load(stream);
        }

        Properties producerConfig = new Properties();
        try (InputStream stream = DataGeneratorJob.class.getClassLoader().getResourceAsStream("producer.properties")) {
            producerConfig.load(stream);
        }

        KafkaSource<FlightData> flightDataSource = KafkaSource.<FlightData>builder()
                .setProperties(consumerConfig)
                .setTopics("flightdata")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(FlightData.class))
                .build();

        DataStreamSource<FlightData> flightDataStream = env
                .fromSource(flightDataSource, WatermarkStrategy.forMonotonousTimestamps(), "flightdata_source");

        KafkaRecordSerializationSchema<UserStatistics> statisticsSerializer = KafkaRecordSerializationSchema.<UserStatistics>builder()
                .setTopic("userstatistics")
                .setValueSerializationSchema(new JsonSerializationSchema<>(
                        () -> new ObjectMapper().registerModule(new JavaTimeModule())
                ))
                .build();

        KafkaSink<UserStatistics> statsSink = KafkaSink.<UserStatistics>builder()
                .setKafkaProducerConfig(producerConfig)
                .setRecordSerializer(statisticsSerializer)
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        defineWorkflow(flightDataStream)
                .sinkTo(statsSink)
                .name("userstatistics_sink")
                .uid("userstatistics_sink");

        env.execute("UserStatistics");
    }

    public static DataStream<UserStatistics> defineWorkflow(
            DataStream<FlightData> flightDataSource
    ) {
        return flightDataSource
                .map(UserStatistics::new)
                .keyBy(UserStatistics::getEmailAddress)
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .reduce(UserStatistics::merge, new ProcessUserStatisticsFunction());
    }
}
