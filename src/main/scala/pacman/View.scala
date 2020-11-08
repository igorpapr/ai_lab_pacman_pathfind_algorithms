package pacman

import breeze.linalg.DenseMatrix
import scalafx.application.JFXApp
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.{Node, Scene}

case class View(model: Model) extends JFXApp.PrimaryStage {

    import View._

    title.value = "Pacman"

    scene = new Scene {
        fill = Color.Black
        content = modelToNodeSeq(model)
    }

    def redraw(): Unit = {
        scene = new Scene {
            fill = Color.Black
            content = modelToNodeSeq(model)
        }
    }
}

object View {

    import pacman.MovingEntity.Pacman

    val CellSize = 24

    case class CellView(cell: Cell, i: Int, j: Int) extends Rectangle {
        width = CellSize
        height = CellSize

        x = i * CellSize
        y = j * CellSize

        cell match {
            case Cell.Empty => fill = Color.Black
            case Cell.Candy =>
                fill = Color.HotPink
                arcWidth = 10
                arcHeight = 10

                width = CellSize / 2
                height = CellSize / 2
                x = i * CellSize + CellSize / 4
                y = j * CellSize + CellSize / 4
            case Cell.Block =>
                fill = Color.Red
                arcWidth = 10
                arcHeight = 10
        }
    }

    def deskToNodeSeq(desk: DenseMatrix[Cell]): Seq[CellView] = for {
        i <- 0 until desk.cols
        j <- 0 until desk.rows
    } yield CellView(desk(i, j), i, j)

    def pacmanToNode(pacman: Pacman): Node = new Circle {
        centerX = pacman.x * CellSize + CellSize / 2
        centerY = pacman.y * CellSize + CellSize / 2
        radius = CellSize / 2 - 2
        fill = Color.Yellow
    }

    def modelToNodeSeq(model: Model): Seq[Node] = deskToNodeSeq(model.desk) :+ pacmanToNode(model.pacman)
}