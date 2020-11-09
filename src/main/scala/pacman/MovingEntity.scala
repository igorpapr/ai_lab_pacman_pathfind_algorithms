package pacman

import pacman.MovingEntity.{Ghost, Pacman}

sealed trait MovingEntity {
    var x: Int
    var y: Int

    def copy(a: Int = x, b: Int = y): MovingEntity = this match {
        case MovingEntity.Pacman(x, y) => Pacman(a, b)
        case MovingEntity.Ghost(x, y) => Ghost(a, b)
    }
}

object MovingEntity {

    case class Pacman(var x: Int, var y: Int) extends MovingEntity

    case class Ghost(var x: Int, var y: Int) extends MovingEntity

    def unapply(arg: MovingEntity): (Int, Int) = (arg.x, arg.y)
}
