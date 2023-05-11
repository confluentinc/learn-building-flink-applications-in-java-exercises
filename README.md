# Building Apache Flink Applications in Java

This repository is for the **Building Flink Applications in Java** course provided by Confluent Developer.

[https://developer.confluent.io/courses/flink-java](https://developer.confluent.io/courses/flink-java)

## Requirements

- Java 11 
	- Flink does not currently support anything newer than Java 11. The code in the repo assumes that you are using Java 11.
- A Java development environment.
- A Flink installation.
- A Confluent Cloud account.

## Gitpod

A [Gitpod](https://gitpod.io/) configuration is available for these exercises. You can use this to construct a pre-configured environment suitable for working on the exercises:

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/confluentinc/learn-building-flink-applications-in-java-exercises)

## Repo Structure

### build.sh

This script is intended for use by the course developers. Students can ignore it.

The `build.sh` script can be used to validate the code across all exercises and solutions.

```bash
./build.sh validate
```

This will stage and then solve each exercise before running the tests for that exercise. It will then move on to the next.

### install_flink.sh

This script is used by the `.gitpod.yml` to install Flink. However, if you are setting up a local environment, you may want to refer to this script (or even execute it) to get your own Flink installation ready.

### cheatsheet.md

A cheatsheet containing code snippets from the course lectures can be found in:

[cheatsheet.md](cheatsheet.md)

### exercises

This folder is where you should be doing your work. Initially it contains just a helper script (`exercise.sh`) but as you advance through the exercises it will contain more.

### exercises/exercise.sh

The `exercise.sh` script is there to help you advance through the exercises. At the beginning of each exercise, you can import any new code by running:

```bash
./exercise.sh stage <exercise number>
```

You can automatically solve an exercise by running:

```bash
./exercise.sh solve <exercise number>
```

**Note:** Solving an exercise will overwrite your code.

You can solve a single file by running:

```bash
./exercise.sh solve <exercise number> <file name>
```

### solutions

This folder contains the solutions for each exercise. You can use these as a reference if you get stuck. It is also used by the `exercise.sh` script to automatically solve exercises.

### staging

This folder is used by the `exercise.sh` script to set up each exercise. You can ignore it.