import pacman.Direction.Directions
import pacman._

import scala.collection.mutable

object MiniMax {
    var iteration = 0
    var models: mutable.HashSet[Model] = mutable.HashSet()

    def alphabeta(model: Model, depth: Int): (Int, List[Model]) =
        alphabeta(model, depth, 0, Int.MinValue, Int.MaxValue, Nil)

    def alphabeta(model: Model, depth: Int, index: Int, alpha: Int, beta: Int, log: List[Model]): (Int, List[Model]) = {
        iteration += 1
        models.add(model)
        //println(s"iteration: $iteration; log len: ${log.length}; unique models: ${models.size}")

        if (depth == 0 || model.state != ModelState.GoingOn) return (modelHeuristic(model) - log.length, log :+ model)
        index match {
            case 0 =>
                var a = alpha
                var value: (Int, List[Model]) = Int.MinValue -> Nil

                for (child <- possibleMoves(model, index)) {
                    value = Seq(value, alphabeta(
                        child,
                        depth - 1,
                        (index + 1) % (model.ghosts.length + 1),
                        a, beta,
                        log :+ child)).maxBy(_._1)
                    a = math.max(a, value._1)
                    if (a >= beta) return value
                }
                value
            case _ =>
                var b = beta
                var value: (Int, List[Model]) = Int.MaxValue -> Nil

                for (child <- possibleMoves(model, index)) {
                    value = Seq(value, alphabeta(
                        child,
                        depth - 1,
                        (index + 1) % (model.ghosts.length + 1),
                        alpha, b,
                        log :+ child)).minBy(_._1)
                    b = math.min(b, value._1)
                    if (b <= alpha) return value
                }
                value
        }
    }

    private def possibleMoves(model: Model, index: Int): LazyList[Model] =
        Directions.flatMap(d => model.moveEntity(index, d))

    private def modelHeuristic(model: Model): Int = model.state match {
        case ModelState.GoingOn => -model.candiesCount
        case ModelState.Win => Int.MaxValue
        case ModelState.Lose => Int.MinValue
    }
}
