package pacman

import breeze.linalg.DenseMatrix
import scalafx.application.JFXApp
import scalafx.scene.Scene
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
    val CELL_SIZE = 20
    val CANDY_SIZE = 6

    case class CellView(cell: Cell, i: Int, j: Int) extends Rectangle {
        width = CELL_SIZE
        height = CELL_SIZE

        x = i * CELL_SIZE
        y = j * CELL_SIZE

        cell match {
            case Cell.Empty => fill = Color.Black
            case Cell.Candy =>
                fill = Color.HotPink
                arcWidth = 10
                arcHeight = 10

                width = CELL_SIZE / 2
                height = CELL_SIZE / 2
                x = i * CELL_SIZE + CELL_SIZE / 4
                y = j * CELL_SIZE + CELL_SIZE / 4
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