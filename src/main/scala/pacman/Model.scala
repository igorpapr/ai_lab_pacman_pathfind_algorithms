package pacman

import pacman.Cell._
import pacman.MovingEntity.{Ghost, Pacman}

case class Model(desk: Matrix[Cell], pacman: Pacman, ghosts: List[Ghost]) {
    lazy val candiesCount: Int = desk.data.count(_ == Candy)
    lazy val state: ModelState = {
        val pxy = (pacman.x, pacman.y)
        if (candiesCount == 0) ModelState.Win
        else if (ghosts.exists(g => (g.x, g.y) == pxy)) ModelState.Lose
        else ModelState.GoingOn
    }

    private def moveEntity[A <: MovingEntity](movingEntity: A, direction: Direction): Option[A] = {
        var (newX, newY) = MovingEntity.unapply(movingEntity)
        direction match {
            case Direction.Up => newY -= 1
            case Direction.Down => newY += 1
            case Direction.Left => newX -= 1
            case Direction.Right => newX += 1
        }
        val xRange = 0 until desk.cols
        val yRange = 0 until desk.rows
        if (!xRange.contains(newX) || !yRange.contains(newY) || desk(newX, newY) == Cell.Block) return None
        val newEntity: A = movingEntity.move(newX, newY).asInstanceOf[A]
        Some(newEntity)
    }

    def moveEntity(index: Int, direction: Direction): Option[Model] = for {
        model <- index match {
            case 0 =>
                moveEntity(pacman, direction).map(p => this.copy(pacman = p))
            case _ =>
                val n = index - 1
                moveEntity(ghosts(n), direction).map(g => this.copy(ghosts = ghosts.updated(n, g)))
        }
    } yield model.eatCandy

    def eatCandy: Model = this.copy(desk = desk.updated(pacman.x, pacman.y, Empty))
}

object Model {
    val default: Model = {
        val desk: Matrix[Cell] = Matrix(15, 15, {
            Vector(
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
                Block, Empty, Empty, Empty, Candy, Candy, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Block, Block, Block, Block, Block, Block, Block, Block, Block, Block, Empty, Empty, Empty, Empty, Empty
            )
        })
        val pacman: Pacman = Pacman(7, 11)
        val ghosts: List[Ghost] = Ghost(8, 12) :: Ghost(8,13) :: Nil
        Model(desk, pacman, ghosts)
    }
}
