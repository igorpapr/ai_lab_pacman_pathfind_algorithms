package pacman

import breeze.linalg.DenseMatrix
import pacman.MovingEntity._

case class Model(desk: DenseMatrix[Cell], pacman: Pacman, ghosts: List[Ghost]) {

    def setDesk(newDesk: DenseMatrix[Cell]): Model = this.copy(desk = newDesk)

    def setPacman(newPacman: Pacman): Model = this.copy(pacman = newPacman)

    def setDesk(newGhosts: List[Ghost]): Model = this.copy(ghosts = newGhosts)

    // Tries to move entity in given direction
    // If movement was successful returns direction, else nothing
    private def moveEntity(entity: MovingEntity, direction: Direction): Option[Direction] = {
        var (newX, newY) = MovingEntity.unapply(entity)
        direction match {
            case Direction.Up => newY -= 1
            case Direction.Down => newY += 1
            case Direction.Left => newX -= 1
            case Direction.Right => newX += 1
        }
        val xRange = 0 until desk.cols
        val yRange = 0 until desk.rows
        if (!xRange.contains(newX) || !yRange.contains(newY) || desk(newX, newY) == Cell.Block) return None
        entity.x = newX
        entity.y = newY
        Some(direction)
    }

    def movePacman(direction: Direction): Option[Direction] = moveEntity(pacman, direction)

}

object Model {

    val default: Model = {
        val desk: DenseMatrix[Cell] = new DenseMatrix(15, 15, {
            import Cell._
            Array(
                Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Empty, Block, Block, Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Empty, Block, Block, Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Empty, Block, Block, Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Block, Block, Block, Block, Block, Block, Block, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Candy, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Block, Block, Block, Block, Block, Block, Block, Block, Block, Block, Empty, Empty, Empty, Empty, Empty
            ): Array[Cell]
        })
        val ghosts: List[Ghost] = Nil
        val pacman: Pacman = Pacman(11, 7)
        Model(desk, pacman, ghosts)
    }

    //    def random(seed: Int = 0): Model = ???
}