package pacman

import breeze.linalg.DenseMatrix
import pacman.Model.{Ghost, MovingEntity, Pacman}

case class Model(desk: DenseMatrix[Cell], pacman: Pacman, ghosts: List[Ghost]) {
    // Tries to move entity in given direction
    // If movement was successful returns direction, else nothing
    private def move_entity(entity: MovingEntity, direction: Direction): Option[Direction] = {
        var (new_x, new_y) = MovingEntity.unapply(entity)
        direction match {
            case Direction.Up => new_y -= 1
            case Direction.Down => new_y += 1
            case Direction.Left => new_x -= 1
            case Direction.Right => new_x += 1
        }
        val x_range = 0 until desk.cols
        val y_range = 0 until desk.rows
        if (!x_range.contains(new_x) || !y_range.contains(new_y) || desk(new_x, new_y) == Cell.Block) return None
        entity.x = new_x
        entity.y = new_y
        Some(direction)
    }

    def move_pacman(direction: Direction): Option[Direction] = move_entity(pacman, direction)

}

object Model {

    sealed trait MovingEntity {
        var x: Int
        var y: Int
    }

    object MovingEntity {
        def unapply(arg: MovingEntity): (Int, Int) = (arg.x, arg.y)
    }

    case class Pacman(var x: Int, var y: Int) extends MovingEntity

    case class Ghost(var x: Int, var y: Int) extends MovingEntity


    def default: Model = {
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
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Block, Empty,
                Block, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
                Block, Block, Block, Block, Block, Block, Block, Block, Block, Block, Empty, Empty, Empty, Empty, Empty
            ): Array[Cell]
        }).t
        val ghosts: List[Ghost] = Nil
        val pacman: Pacman = Pacman(11, 7)
        Model(desk, pacman, ghosts)
    }

    def random(seed: Int = 0): Model = ???
}