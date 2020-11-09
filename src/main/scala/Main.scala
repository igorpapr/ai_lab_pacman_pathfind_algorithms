import pacman.{Model, View}
import scalafx.application.JFXApp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends JFXApp {
    val model: Model = Model.default
    val view: View = View(model)
    stage = view
    Future {
        //        Thread.sleep(500)
        //        println(model.movePacman(Direction.Down))
        //        Platform.runLater(() => view.redraw())

        println(MiniMax.minimax(model, 20))
    }
}
