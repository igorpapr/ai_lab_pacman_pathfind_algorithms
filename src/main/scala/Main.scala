import javafx.application.Platform
import pacman.{Direction, Model, View}
import scalafx.application.JFXApp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends JFXApp {
    val model = Model.default
    val view = View(model)
    stage = view
    Future {
        Thread.sleep(500)
        println(model.movePacman(Direction.Down))
        Platform.runLater(() => view.redraw())
    }
}
