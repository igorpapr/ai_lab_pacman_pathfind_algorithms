import pacman.Direction.Directions
import pacman._

object MiniMax {

    def minimax(model: Model, depth: Int): (Int, List[Direction]) =
        minimax(model, depth, 0, Nil)

    def minimax(model: Model, depth: Int, index: Int, path: List[Direction]): (Int, List[Direction]) = {
        println(modelHeuristic(model), path, model.state)
        if (depth == 0 || model.state != ModelState.GoingOn) return (modelHeuristic(model), path)
        index match {
            case 0 =>
                possibleMoves(model, 0)
                  .map { case (model, direction) =>
                      minimax(model, depth - 1, (index + 1) % (model.ghosts.length + 1), path :+ direction)
                  }.maxBy(_._1)
            case _ =>
                possibleMoves(model, index)
                  .map { case (model, direction) =>
                      minimax(model, depth - 1, (index + 1) % (model.ghosts.length + 1), path :+ direction)
                  }.minBy(_._1)
        }
    }

    def possibleMoves(model: Model, index: Int): List[(Model, Direction)] =
        Directions.flatMap(d => model.moveEntity(index, d))

    def modelHeuristic(model: Model): Int = model.state match {
        case ModelState.GoingOn => -model.candiesCount
        case ModelState.Win => Int.MaxValue
        case ModelState.Lose => Int.MinValue
    }

    //    def minimax(model: Model): (Int, List[Direction]) = {
    //        val depth = model.desk.size / 2
    //        minimax(model, depth)
    //    }
    //
    //    def minimax(model: Model, depth: Int): (Int, List[Direction]) = {
    //        minimax(model, depth, 0, Nil)
    //    }
    //
    //    private def minimax(model: Model,
    //                        depth: Int,
    //                        i: Int,
    //                        path: List[Direction]): (Int, List[Direction]) = {
    //        println(path)
    //        if (depth == 0 || model.checkState() == ModelState.Win) return (modelValue(model), path)
    //        val x =
    //            if (i == 0) model.pacman
    //            else model.ghosts(i)
    //        x match {
    //            case pacman: MovingEntity.Pacman =>
    //                possibleMoves(model, pacman)
    //                  .map { case (d, ch) => minimax(ch, depth - 1, (i+1) % (model.ghosts.length + 1), path :+ d) }
    //                  .maxBy(_._1)
    //            case ghost: MovingEntity.Ghost =>
    //                possibleMoves(model, ghost)
    //                  .map { case (d, ch) => minimax(ch, depth - 1, (i+1) % (model.ghosts.length + 1), path :+ d) }
    //                  .minBy(_._1)
    //        }
    //    }
    //
    //    private def possibleMoves(model: Model, movingEntity: MovingEntity): List[(Direction, Model)] = {
    //        Directions.flatMap(d => {
    //            model.moveEntity(movingEntity, d)
    //        })
    //    }
    //
    //    private def modelValue(model: Model): Int = {
    //        val state = model.checkState()
    //        state match {
    //            case ModelState.Win => 1000
    //            case ModelState.Lose => -1000
    //            case _ => 0
    //        }
    //    }
}
