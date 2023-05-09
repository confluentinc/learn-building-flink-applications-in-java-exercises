package datagen;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.formats.json.JsonSerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.util.Properties;

public class DataGeneratorJob {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties producerConfig = new Properties();
		try (InputStream stream = DataGeneratorJob.class.getClassLoader().getResourceAsStream("producer.properties")) {
			producerConfig.load(stream);
		}

		DataGeneratorSource<SkyOneAirlinesFlightData> skyOneSource =
				new DataGeneratorSource<>(
						index -> DataGenerator.generateSkyOneAirlinesFlightData(),
						Long.MAX_VALUE,
						RateLimiterStrategy.perSecond(1),
						Types.POJO(SkyOneAirlinesFlightData.class)
				);

		DataStream<SkyOneAirlinesFlightData> skyOneStream = env
				.fromSource(skyOneSource, WatermarkStrategy.noWatermarks(), "skyone_source");

		KafkaRecordSerializationSchema<SkyOneAirlinesFlightData> skyOneSerializer = KafkaRecordSerializationSchema.<SkyOneAirlinesFlightData>builder()
				.setTopic("skyone")
				.setValueSerializationSchema(new JsonSerializationSchema<>(DataGeneratorJob::getMapper))
				.build();

		KafkaSink<SkyOneAirlinesFlightData> skyOneSink = KafkaSink.<SkyOneAirlinesFlightData>builder()
				.setKafkaProducerConfig(producerConfig)
				.setRecordSerializer(skyOneSerializer)
				.setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
				.build();

		skyOneStream
			.sinkTo(skyOneSink)
			.name("skyone_sink");

		DataGeneratorSource<SunsetAirFlightData> sunsetSource =
				new DataGeneratorSource<>(
						index -> DataGenerator.generateSunsetAirFlightData(),
						Long.MAX_VALUE,
						RateLimiterStrategy.perSecond(1),
						Types.POJO(SunsetAirFlightData.class)
				);

		DataStream<SunsetAirFlightData> sunsetStream = env
				.fromSource(sunsetSource, WatermarkStrategy.noWatermarks(), "sunset_source");

		KafkaRecordSerializationSchema<SunsetAirFlightData> sunSetSerializer = KafkaRecordSerializationSchema.<SunsetAirFlightData>builder()
				.setTopic("sunset")
				.setValueSerializationSchema(new JsonSerializationSchema<>(DataGeneratorJob::getMapper))
				.build();

		KafkaSink<SunsetAirFlightData> sunsetSink = KafkaSink.<SunsetAirFlightData>builder()
				.setKafkaProducerConfig(producerConfig)
				.setRecordSerializer(sunSetSerializer)
				.setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
				.build();

		sunsetStream
				.sinkTo(sunsetSink)
				.name("sunset_sink");

		env.execute("InputStreams");
	}

	private static ObjectMapper getMapper() {
		return new ObjectMapper().registerModule(new JavaTimeModule());
	}
}
