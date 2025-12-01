# Changelog

## Version 0.1.0

* Initial commit of required files for a public repo.

## Version 1.0.0

* Initial commit of exercise code.

## Version 1.1.0

* Enhanced the [README.md](README.md)
* Added the [cheatsheet.md](cheatsheet.md)
* Added the `build.sh`
* Updated the `.gitignore`
* Added a pull request template for Github.

## Version 1.1.1

* Upgrade to Flink 1.17.1

## Version 2.0

* Implemented Exercise 16 - Merging Streams.
* Implemented Exercise 18 - Aggregating Flink Data using Windowing.
* Implemented Exercise 20 - Managing State in Flink.

## Version 2.0.1

* Added additional tests to verify the serialization/deserialization of the models, including one for testing the use of the JsonIgnoreProperties annotation.

## Version 2.1.0

* Removed Gitpod from the project.

## Version 3.0.0

* Upgraded to Apache Flink 2.1.1 (latest stable release with Java 21 support)
* Upgraded to Java 21
* Updated all dependencies:
  - flink-connector-base: 2.1.1 (required for DeliveryGuarantee)
  - Kafka connector: 4.0.1-2.0
  - Kafka clients: 3.9.0
  - Jackson: 2.18.2
  - Log4j: 2.24.3
  - JUnit: 5.11.4
* Updated Maven plugins:
  - maven-compiler-plugin: 3.13.0
  - maven-surefire-plugin: 3.5.2
  - maven-shade-plugin: 3.6.0
* Updated Flink installation scripts to download Flink 2.1.1
* API Changes for Flink 2.x compatibility:
  - Removed flink-java dependency (no longer exists in Flink 2.x)
  - Replaced Time API with java.time.Duration (Time.minutes(1) → Duration.ofMinutes(1))
  - Updated open() method signature to use OpenContext instead of Configuration
* Updated all Java source files and documentation for Flink 2.x APIs