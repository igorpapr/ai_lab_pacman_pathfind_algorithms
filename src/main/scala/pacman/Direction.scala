package pacman

sealed trait Direction

object Direction {

    case object Up extends Direction

    case object Down extends Direction

    case object Left extends Direction

    case object Right extends Direction

    val Directions: LazyList[Direction] = LazyList(Up, Down, Left, Right)

}