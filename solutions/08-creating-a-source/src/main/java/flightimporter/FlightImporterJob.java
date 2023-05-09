package flightimporter;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
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

        KafkaSource<String> skyOneSource = KafkaSource.<String>builder()
            .setProperties(consumerConfig)
            .setTopics("skyone")
            .setStartingOffsets(OffsetsInitializer.latest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();

        DataStream<String> skyOneStream = env
            .fromSource(skyOneSource, WatermarkStrategy.noWatermarks(), "skyone_source");

        skyOneStream.print();

        env.execute("FlightImporter");
    }
}