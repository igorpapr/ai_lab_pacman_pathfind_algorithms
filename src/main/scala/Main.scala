import java.util.concurrent.TimeUnit

import pacman.{Model, ModelState, View}
import scalafx.application.{JFXApp, Platform}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends JFXApp {
    val initModel: Model = Model.fullCandies(Model.default)
    val depth = initModel.ghosts.length+1
    val view: View = View(initModel)
    stage = view
    Future {
        run(initModel)
    }.recover(e => {
        e.printStackTrace()
        println(e)
    })

    def time[R](block: => R): R = {
        val t0 = System.nanoTime()
        val result = block // call-by-name
        val t1 = System.nanoTime()
        println("Elapsed time: " + TimeUnit.NANOSECONDS.toMillis(t1 - t0) + "ms")
        result
    }

    @tailrec
    def run(model: Model, index: Int = 0): Unit = {
        if (model.state != ModelState.GoingOn) return
        val (value, log) = time {
            MiniMax.alphabeta(model, depth, index)
        }
        if (log.isEmpty) {
            println("Pacman stuck :(")
            return
        }
        val newModel = log.last
        println(value, newModel.state, log.length)

        log.foreach { m =>
            Thread.sleep(50)
            Platform.runLater(view.redraw(m))
        }

        run(newModel, (index + 1) % (model.ghosts.length + 1))
    }
}
