# Building Apache Flink Applications in Java - Cheatsheet

## The Flink Lifecycle

### A Simple Job

```java
public static void main(String[] args) throws Exception {
	StreamExecutionEnvironment env = StreamExecutionEnvironment
		.getExecutionEnvironment();
	
	env.fromElements(1,2,3,4,5).print();
	
	env.execute();
}
```

### Running a Job

```bash
$ flink run $JAR_FILE
```

```bash
$ flink run -c mypackage.MyClass $JAR_FILE
```

```bash
$ flink run --detached $JAR_FILE
```

### Canceling a Job

```bash
$ flink cancel $JOB_ID
```

### Stopping a Job

```bash
$ flink stop --savepointPath $SAVEPOINT_FOLDER $JOB_ID
```

### Resuming a Job

```bash
$ flink run --fromSavepoint $SAVEPOINT_FILE $JAR_FILE
```

### Setting a Restart Strategy

```java
env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
        3, // number of restart attempts
        Time.of(10, TimeUnit.SECONDS) // delay
));
```

## Flink Data Sources

### StreamExecutionEnvironment.fromElements

```java
DataStream<Integer> stream = env.fromElements(1,2,3,4,5);
```

### DataGeneratorSource

```java
DataGeneratorSource<String> source =
	new DataGeneratorSource<>(
			index -> "String"+index,
			numRecords,
			RateLimiterStrategy.perSecond(1),
			Types.STRING
	);
```

### FileSource

```java
FileSource<String> source =
	FileSource.forRecordStreamFormat(
		new TextLineInputFormat(),
		new Path("<PATH_TO_FILE>")
	).build();
```

### KafkaSource

```java
KafkaSource<String> source = KafkaSource.<String>builder()
	.setProperties(config)
	.setTopics("topic1", "topic2")
	.setValueOnlyDeserializer(new SimpleStringSchema())
	.build();
```

### DataStream.fromSource

```java
DataStream<String> stream = env
    .fromSource(
    	source,
    	WatermarkStrategy.noWatermarks(), 
    	"my_source"
	);
```

### DataStream.print

```java []
stream.print();
```

## Serializers & Deserializers

### A Simple POJO
```java
public class Person {
	public String name;
	private String email;

	public Person() {}
	
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
}
```

### Registring Kryo Serializers

```java
env.getConfig().registerKryoType(MyCustomType.class);
```

### Disabling Kryo Serialization

```java
env.getConfig().disableGenericTypes();
```

### JsonSerializationSchema

```java
JsonSerializationSchema<MyClass> serializer = 
	new JsonSerializationSchema<>();
```

### JsonDeserializationSchema

```java
JsonDeserializationSchema<MyClass> deserializer = 
	new JsonDeserializationSchema<>(MyClass.class);
```

### Custom Object Mapper

```java
JsonSerializationSchema<MyClass> serializer = 
	new JsonSerializationSchema<>(() -> 
		new ObjectMapper()
			.registerModule(new JavaTimeModule())
	);
```

## Transforming Data in Flink

### ProcessFunction - Mapping Elements

```java
public class MyProcessFunction 
  extends ProcessFunction<Input, Output> {
  @Override
  public void processElement(
    Input input,
    ProcessFunction<Input, Output>.Context ctx,
    Collector<Output> collector
  ) {
    collector.collect(new Output(input));
  }
}
```

### ProcessFunction - Flattening Mapped Elements

```java
public class MyProcessFunction 
  extends ProcessFunction<Input[], Output> {
  @Override
  public void processElement(
    Input[] collection,
    ProcessFunction<Input[], Output>.Context ctx,
    Collector<Output> collector
  ) {
    for(Input input : collection) {
      collector.collect(new Output(input));
    }
  }
}
```

### ProcessFunction - Filtering Elements

```java
  public class MyProcessFunction 
  extends ProcessFunction<Input, Input> {
  @Override
  public void processElement(
    Input input,
    ProcessFunction<Input, Input>.Context ctx,
    Collector<Input> collector
  ) {
    if(condition) {
      collector.collect(input);
    }
  }
}
```

### Process

```java
stream.process(new MyProcessFunction());
```

### Map

```java
stream.map(input -> new Output(input));
```

```java
DataStream<Double> doubles = integers.map(
	input -> Double.valueOf(input) / 2
);
```

### FlatMap

```java
stream.flatMap((collection,collector) -> {
	for(Input input: collection) {
		collector.collect(new Output(input));
	}
});
```

```java
DataStream<Integer> letterCount = sentences
	.map(input -> input.split(" "))
	.flatMap((words, collector) -> {
		for (String word : words) {
			collector.collect(word.length());
		}
	});
```

### Filter

```java
stream.filter(input -> condition);
```

```java
DataStream<Integer> evenIntegers = integers
	.filter(input -> input % 2 == 0);
```

### KeyBy

```java
stream.keyBy(
	input -> input.getKey()
)
```

### KeyedProcessFunction

```java
class MyKeyedProcessFunction
  extends KeyedProcessFunction<String, Input, Output> {
  @Override
  public void processElement(
    Input input,
    KeyedProcessFunction<String, Input, Output>.Context ctx,
    Collector<Output> collector) {
      String key = ctx.getCurrentKey();
      ...
  }
}
```

### Reduce

```java
stream
  .keyBy(input -> input.key)
  .reduce((s1, s2) -> s1.merge(s2));
```

```java
DataStream<Tuple2<String, Integer>> wordCountsByFirstLetter = 
	itemIdsAndCounts
		.keyBy(tuple -> tuple.f0)
		.reduce((l1, l2) -> new Tuple2(l1.f0, l1.f1 + l2.f1));
```

## Flink Data Sinks

### KafkaRecordSerializer

```java
KafkaRecordSerializationSchema<MyClass> serializer = 
	KafkaRecordSerializationSchema.<MyClass>builder()
		.setTopic("topic_name")
		.setValueSerializationSchema(
			new JsonSerializationSchema<>()
		)
		.build();
```

### KafkaSink

```java
KafkaSink<MyClass> sink = KafkaSink.<MyClass>builder()
	.setKafkaProducerConfig(config)
	.setRecordSerializer(serializer)
	.setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
	.build();
```

### Using a Sink

```java
stream
	.sinkTo(sink)
	.name("sink_name");
```
