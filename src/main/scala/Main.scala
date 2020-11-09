import pacman.{Direction, Model, View}
import scalafx.application.{JFXApp, Platform}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends JFXApp {
    val model: Model = Model.default
    val view: View = View(model)
    stage = view
    Future {
        Thread.sleep(500)
        val model1 = model.moveEntity(0, Direction.Down).get._1
        Platform.runLater(view.redraw(model1))
        println(model1.state)
        println(model1.desk(model1.pacman.x, model1.pacman.y))
        //                println(MiniMax.minimax(model, 3))
//        println(MiniMax.minimax(model, 5))
    }

}
