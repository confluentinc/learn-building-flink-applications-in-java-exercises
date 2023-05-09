package flightimporter;

import datagen.DataGeneratorJob;
import models.FlightData;
import models.SkyOneAirlinesFlightData;
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
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Properties;

public class FlightImporterJob {

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

        KafkaSource<SkyOneAirlinesFlightData> skyOneSource = KafkaSource.<SkyOneAirlinesFlightData>builder()
                .setProperties(consumerConfig)
                .setTopics("skyone")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(SkyOneAirlinesFlightData.class))
                .build();

        DataStream<SkyOneAirlinesFlightData> skyOneStream = env
                .fromSource(skyOneSource, WatermarkStrategy.noWatermarks(), "skyone_source");

        KafkaRecordSerializationSchema<FlightData> flightSerializer = KafkaRecordSerializationSchema.<FlightData>builder()
                .setTopic("flightdata")
                .setValueSerializationSchema(new JsonSerializationSchema<>(
                        () -> new ObjectMapper().registerModule(new JavaTimeModule())
                ))
                .build();

        KafkaSink<FlightData> flightSink = KafkaSink.<FlightData>builder()
                .setKafkaProducerConfig(producerConfig)
                .setRecordSerializer(flightSerializer)
                .build();

        defineWorkflow(skyOneStream)
            .sinkTo(flightSink)
            .name("flightdata_sink");

        env.execute("FlightImporter");
    }

    public static DataStream<FlightData> defineWorkflow(
            DataStream<SkyOneAirlinesFlightData> skyOneSource
    ) {
        DataStream<FlightData> skyOneFlightStream = skyOneSource
                .filter(flight -> flight.getFlightArrivalTime().isAfter(ZonedDateTime.now()))
                .map(SkyOneAirlinesFlightData::toFlightData);

        return skyOneFlightStream;
    }
}
