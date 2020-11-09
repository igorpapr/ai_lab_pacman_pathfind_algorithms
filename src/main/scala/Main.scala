import java.util.concurrent.TimeUnit

import pacman.{Model, View}
import scalafx.application.{JFXApp, Platform}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends JFXApp {
    val model: Model = Model.default
    val view: View = View(model)
    stage = view
    Future {
        def time[R](block: => R): R = {
            val t0 = System.nanoTime()
            val result = block    // call-by-name
            val t1 = System.nanoTime()
            println("Elapsed time: " + TimeUnit.NANOSECONDS.toSeconds(t1 - t0) + "s")
            result
        }

        val (_, log) = time { MiniMax.alphabeta(model, 10) }
        log.foreach { m =>
            Thread.sleep(500)
            Platform.runLater(view.redraw(m))
        }
        println(log.last.state)
    }

}
