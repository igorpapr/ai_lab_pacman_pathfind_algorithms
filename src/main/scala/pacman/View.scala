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

    case class CellView(cell: Cell, i: Int, j: Int) {
        val Empty: Node = new Rectangle {
            x = i * CellSize
            y = j * CellSize
            height = CellSize
            width = CellSize
            fill = Color.Transparent
        }
        val nodes: Seq[Node] = Empty +: (cell match {
            case Cell.Empty => Nil
            case Cell.Block => Seq(new Rectangle {
                x = i * CellSize
                y = j * CellSize
                width = CellSize
                height = CellSize
                fill = Color.Red
                arcWidth = 10
                arcHeight = 10
            })
            case Cell.Candy => Seq(new Circle {
                fill = Color.HotPink
                centerX = i * CellSize + CellSize / 2
                centerY = j * CellSize + CellSize / 2
                radius = CellSize / 5
            })
        })
    }

    def deskToNodeSeq(desk: DenseMatrix[Cell]): Seq[Node] = (for {
        i <- 0 until desk.cols
        j <- 0 until desk.rows
    } yield CellView(desk(i, j), i, j).nodes).flatten

    def pacmanToNode(pacman: Pacman): Node = new Circle {
        centerX = pacman.x * CellSize + CellSize / 2
        centerY = pacman.y * CellSize + CellSize / 2
        radius = CellSize / 2 - 2
        fill = Color.Yellow
    }

    def modelToNodeSeq(model: Model): Seq[Node] = deskToNodeSeq(model.desk) :+ pacmanToNode(model.pacman)
}