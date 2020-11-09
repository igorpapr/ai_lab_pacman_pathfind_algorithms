package pacman

sealed trait ModelState

object ModelState {

    case object GoingOn extends ModelState

    case object Win extends ModelState

    case object Lose extends ModelState

}
