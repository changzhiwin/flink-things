## What's this
Write Flink streaming App, using Scala(2.12.17) with Java API(1.16.0), due to Scala API is deprecated.

## Enveriment
- Scala 2.12.17
- Flink 1.16.0

## How to run
```
// 1, start Flink
$bin/start-cluster.sh  

// 2, compile
sbt package

// 3, submit app
$bin/flink run -c bigdata.flink.things.AverageSensorReadings ./target/scala-2.12/flink-practice_2.12-1.1.0.jar

// 4, check the result
tail -f ./log/flink-user-taskexecutor-n-hostname.out
```

## API Doc
> https://nightlies.apache.org/flink/flink-docs-release-1.16/api/java/

Scala API is deprecated, see [here](https://cwiki.apache.org/confluence/display/FLINK/FLIP-265+Deprecate+and+remove+Scala+API+support) and [scala free in one fifteen](https://flink.apache.org/2022/02/22/scala-free.html)

- [guide](https://nightlies.apache.org/flink/flink-docs-release-1.16/docs/dev/datastream/overview/)
- [java](https://github.com/apache/flink/tree/release-1.16/flink-streaming-java/src/main/java/org/apache/flink/streaming/api)

## Issue

#### 1, Fixing the Scala error: java.lang.NoSuchMethodError
As a note to self, when you see a Scala error message that looks like this:

>[java.lang.NoSuchMethodError: scala.Product.$init$(Lscala/Product;)V](https://alvinalexander.com/misc/fixing-scala-error-java-lang.nosuchmethoderror-scala.product-init/)


it probably means that you have a mismatch in the Scala versions you’re using in your project. For instance, I just tried to use a library I compiled with Scala 2.12 with Spark, which was compiled with Scala 2.11, and I got that error message. In this case I was able to resolve the problem by recompiling my library with Scala 2.11.