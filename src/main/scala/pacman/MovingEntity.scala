package pacman

sealed trait MovingEntity {
    var x: Int
    var y: Int
}

object MovingEntity {

    case class Pacman(var x: Int, var y: Int) extends MovingEntity

    case class Ghost(var x: Int, var y: Int) extends MovingEntity

    def unapply(arg: MovingEntity): (Int, Int) = (arg.x, arg.y)
}
