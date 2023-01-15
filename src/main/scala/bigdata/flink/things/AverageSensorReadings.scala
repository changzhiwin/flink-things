
package bigdata.flink.things

import scala.collection.JavaConverters._

import org.apache.flink.streaming.api.functions.windowing.WindowFunction
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.api.common.eventtime.{ WatermarkStrategy }
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.util.Collector

import bigdata.flink.things.util.{SensorReading, SensorSource, SensorTimeAssigner}

// Ref: https://nightlies.apache.org/flink/flink-docs-release-1.16/docs/dev/configuration/overview/
// Ref: https://nightlies.apache.org/flink/flink-docs-release-1.16/docs/dev/datastream/overview/

object AverageSensorReadings {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment()

    // Default, so no need call
    // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // configure watermark interval
    env.getConfig.setAutoWatermarkInterval(1000L)

    val sensorData: DataStream[SensorReading] = env
      .addSource(new SensorSource)
      .assignTimestampsAndWatermarks(
        WatermarkStrategy
          .forMonotonousTimestamps()
          .withTimestampAssigner(
            new AssignerWithPeriodicWatermarksAdapter.Strategy(new SensorTimeAssigner)
          )
      )

    val avgTemp: DataStream[SensorReading] = sensorData
      .map( r => SensorReading(r.id, r.timestamp, (r.temperature - 32) * (5.0 / 9.0)) )
      .keyBy(_.id)
      // group readings in 1 second windows
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))
      // compute average temperature using a user-defined function
      .apply(new TemperatureAverager)

    avgTemp.print()

    env.execute("Compute average sensor temperature")
  }
}

/** User-defined WindowFunction to compute the average temperature of SensorReadings */
class TemperatureAverager extends WindowFunction[SensorReading, SensorReading, String, TimeWindow] {

  /** apply() is invoked once for each window */
  override def apply(
    sensorId: String,
    window: TimeWindow,
    vals: java.lang.Iterable[SensorReading],
    out: Collector[SensorReading]): Unit = {

    // vals is Java API, so need converte
    val (cnt, sum) = vals.asScala.foldLeft((0, 0.0))((c, r) => (c._1 + 1, c._2 + r.temperature))
    val avgTemp = sum / cnt

    out.collect(SensorReading(sensorId, window.getEnd, avgTemp))
  }
}
