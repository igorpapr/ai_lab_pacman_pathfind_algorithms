import pacman.Direction.Directions
import pacman._

object MiniMax {

    def minimax(model: Model): (Int, List[Direction]) = {
        val depth = model.desk.size / 2
        minimax(model, depth)
    }

    def minimax(model: Model, depth: Int): (Int, List[Direction]) = {
        // MovingEntitieS = mes
        val mes = (model.pacman :: model.ghosts).asInstanceOf[::[MovingEntity]]
        minimax(model, depth, mes, Nil)
    }

    private def minimax(model: Model,
                        depth: Int,
                        movingEntities: ::[MovingEntity],
                        path: List[Direction]): (Int, List[Direction]) = {
        println(path)
        if (depth == 0) return (modelValue(model), path)
        val (x, xs) = ::.unapply(movingEntities).get
        val mes = (model.pacman :: model.ghosts).asInstanceOf[::[MovingEntity]]
        x match {
            case pacman: MovingEntity.Pacman =>
                possibleMoves(model, pacman)
                  .map { case (d, ch) => minimax(ch, depth - 1, mes, path :+ d) }
                  .maxBy(_._1)
            case ghost: MovingEntity.Ghost =>
                possibleMoves(model, ghost)
                  .map { case (d, ch) => minimax(ch, depth - 1, mes, path :+ d) }
                  .minBy(_._1)
        }
    }

    private def possibleMoves(model: Model, movingEntity: MovingEntity): List[(Direction, Model)] = {
        Directions.flatMap(d => {
            val child = model.copy()
            child.moveEntity(movingEntity, d).map(x => (x._1, child))
        })
    }

    private def modelValue(model: Model): Int = {
        val state = model.checkState()
        state match {
            case ModelState.GoingOn => model.cellUnderPacman match {
                case Cell.Candy => 10
                case _ => 0 // actually, cannot be anything than Empty
            }
            case ModelState.Win => 100
            case ModelState.Lose => -100
        }
    }
}
