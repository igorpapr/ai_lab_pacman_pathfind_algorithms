package pacman

import breeze.linalg.DenseMatrix
import pacman.Cell._
import pacman.MovingEntity._

case class Model(desk: DenseMatrix[Cell], pacman: Pacman, ghosts: List[Ghost]) {

    //    def setDesk(newDesk: DenseMatrix[Cell]): Model = this.copy(desk = newDesk)
    //
    //    def setPacman(newPacman: Pacman): Model = this.copy(pacman = newPacman)
    //
    //    def setDesk(newGhosts: List[Ghost]): Model = this.copy(ghosts = newGhosts)
    //
    //    def setDeskFullCandies(): Model = this.copy(desk = {
    //        val newDesk = desk.copy
    //        val pxy = (pacman.x, pacman.y)
    //        for {
    //            i <- 0 until newDesk.cols
    //            j <- 0 until newDesk.rows
    //        } yield {
    //            if (newDesk(i, j) == Empty && (i,j) != pxy) newDesk(i, j) = Candy
    //        }
    //        newDesk
    //    })

    def candiesCount: Int = desk.data.count(_ == Candy)

    // Tries to move entity in given direction
    // If movement was successful returns direction, else nothing
    def moveEntity(entity: MovingEntity, direction: Direction): Option[(Direction, Model)] = {
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
        val newEntity = entity.copy(newX, newY)
        entity.x = newX
        entity.y = newY
        val xs: List[MovingEntity] = (pacman :: ghosts).map(e =>
            if (e == entity) newEntity else e.copy())
        val model = Model(desk.copy, xs.head.asInstanceOf[Pacman], xs.tail.asInstanceOf[List[Ghost]])
        model.checkState()
        Some(direction, model)
    }

    def movePacman(direction: Direction): Option[(Direction, Model)] = moveEntity(pacman, direction)

    def checkState(): ModelState = {
        val pxy = (pacman.x, pacman.y)
        if (desk(pxy) == Candy)
            desk(pxy) = Empty
        if (candiesCount == 0) return ModelState.Win
        if (ghosts.exists(g => (g.x, g.y) == pxy)) return ModelState.Lose
        ModelState.GoingOn
    }

    def cellUnderPacman: Cell = desk(pacman.x, pacman.y)

}

object Model {

    val default: Model = {
        val desk: DenseMatrix[Cell] = new DenseMatrix(15, 15, {
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
        val pacman: Pacman = Pacman(7, 11)
        Model(desk, pacman, ghosts)
    }

    //    def random(seed: Int = 0): Model = ???
}