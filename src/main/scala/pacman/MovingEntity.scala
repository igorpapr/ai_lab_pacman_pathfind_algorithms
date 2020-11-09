package pacman

import pacman.MovingEntity.{Ghost, Pacman}

sealed trait MovingEntity {
    val x: Int
    val y: Int

    def move(newX: Int, newY: Int): MovingEntity
}

object MovingEntity {

    case class Pacman(x: Int, y: Int) extends MovingEntity {
        def move(newX: Int, newY: Int): Pacman = copy(newX, newY)
    }

    case class Ghost(x: Int, y: Int) extends MovingEntity {
        def move(newX: Int, newY: Int): Ghost = copy(newX, newY)
    }

    def unapply(arg: MovingEntity): (Int, Int) = (arg.x, arg.y)
}
