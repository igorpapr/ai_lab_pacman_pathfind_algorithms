import pacman.{Model, View}
import scalafx.application.JFXApp

object Main extends JFXApp {
    stage = View(Model.default)
}
