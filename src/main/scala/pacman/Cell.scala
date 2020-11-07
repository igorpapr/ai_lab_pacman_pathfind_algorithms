package pacman

sealed trait Cell

object Cell {

    case object Empty extends Cell

    case object Candy extends Cell

    case object Block extends Cell

}