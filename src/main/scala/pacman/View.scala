package pacman

import breeze.linalg.DenseMatrix
import scalafx.application.JFXApp
import scalafx.scene.{Node, Scene}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

case class View(model: Model) extends JFXApp.PrimaryStage {

    import View._

    title.value = "Pacman"

    scene = new Scene {
        fill = Color.Black
        content = deskToCellViewSeq(model.desk)
    }
}

object View {

    import pacman.MovingEntity.{Pacman, Ghost}

    val CellSize = 20

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

    def deskToCellViewSeq(desk: DenseMatrix[Cell]): Seq[CellView] = for {
        i <- 0 until desk.cols
        j <- 0 until desk.rows
    } yield CellView(desk(i, j), i, j)
}