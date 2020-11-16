import java.util.concurrent.TimeUnit

import pacman.{Model, ModelState, View}
import scalafx.application.{JFXApp, Platform}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends JFXApp {
    val initModel: Model = Model.fullCandies(Model.default)
//    val depth = initModel.ghosts.length+1
    val depth = 6
    val view: View = View(initModel)
    stage = view
    Future {
        val (log, iterations) = time {run(initModel)}
        println(s"Total alphabeta calls: $iterations")
        log.foreach { m =>
            Thread.sleep(75)
            Platform.runLater(view.redraw(m))
        }
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
    def run(model: Model, index: Int = 0, totalLog: List[Model] = Nil, iterations: Int = 0): (List[Model], Int) = {
        if (model.state != ModelState.GoingOn) return totalLog -> iterations
        val (value, log) = MiniMax.alphabeta(model, depth, index)
        if (log.isEmpty) {
            println("Pacman stuck :(")
            return totalLog -> iterations
        }
        val newModel = log.last
        println(value, newModel.state, log.length)

        run(newModel, (index + 1) % (model.ghosts.length + 1), totalLog ++ log, iterations+1)
    }
}
