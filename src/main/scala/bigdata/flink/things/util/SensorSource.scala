package bigdata.flink.things.util

import java.util.Calendar
import scala.util.Random

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

class SensorSource extends RichParallelSourceFunction[SensorReading] {

  var running: Boolean = true

  override def run(srcCtx: SourceContext[SensorReading]): Unit = {
    val rand = new Random()

    val taskIdx = this.getRuntimeContext().getIndexOfThisSubtask()

    var curFTemp = (1 to 10).map {
      i => ("sensor_" + (taskIdx * 10 + i), 65 + (rand.nextGaussian() * 20))
    }

    while (running) {

      curFTemp = curFTemp.map( t => (t._1, t._2 + (rand.nextGaussian() * 0.5)) )
 
      val curTime = Calendar.getInstance.getTimeInMillis
      curFTemp.foreach( t => srcCtx.collect(SensorReading(t._1, curTime, t._2)))

      Thread.sleep(100)
    }

  }

  override def cancel(): Unit = {
    running = false
  }

}
