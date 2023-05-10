package flightimporter;

import models.SkyOneAirlinesFlightData;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.util.Properties;

public class FlightImporterJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties consumerConfig = new Properties();
        try (InputStream stream = FlightImporterJob.class.getClassLoader().getResourceAsStream("consumer.properties")) {
            consumerConfig.load(stream);
        }

        KafkaSource<SkyOneAirlinesFlightData> skyOneSource = KafkaSource.<SkyOneAirlinesFlightData>builder()
            .setProperties(consumerConfig)
            .setTopics("skyone")
            .setStartingOffsets(OffsetsInitializer.latest())
            .setValueOnlyDeserializer(new JsonDeserializationSchema(SkyOneAirlinesFlightData.class))
            .build();

        DataStream<SkyOneAirlinesFlightData> skyOneStream = env
            .fromSource(skyOneSource, WatermarkStrategy.noWatermarks(), "skyone_source");

        skyOneStream.print();

        env.execute("FlightImporter");
    }
}