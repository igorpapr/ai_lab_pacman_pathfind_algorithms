import pacman.Direction.Directions
import pacman._

import scala.collection.mutable

object MiniMax {
    val MinValue: Float = -10000
    val MaxValue: Float = 10000
    var iteration = 0
    var models: mutable.HashSet[Model] = mutable.HashSet()

    def alphabeta(model: Model, depth: Int): (Float, List[Model]) =
        alphabeta(model, depth, 0, MinValue, MaxValue, Nil)

    def alphabeta(model: Model, depth: Int, index: Int, alpha: Float, beta: Float, log: List[Model]): (Float, List[Model]) = {
        iteration += 1
        models.add(model)
//        println(s"iteration: $iteration; log len: ${log.length}; unique models: ${models.size};" +
//          s"model heuristic: ${modelHeuristic(model)}")

        if (depth == 0 || model.state != ModelState.GoingOn)
            return (modelHeuristic(model) - log.length.toFloat, log :+ model)
        index match {
            case 0 =>
                var a = alpha
                var value: (Float, List[Model]) = MinValue -> Nil

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
                var value: (Float, List[Model]) = MaxValue -> Nil

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

    private def modelHeuristic(model: Model): Float = model.state match {
        case ModelState.GoingOn => -model.candiesCount * 10 + distanceToGhosts(model)
        case ModelState.Win => MaxValue + distanceToGhosts(model)
        case ModelState.Lose => MinValue
    }

    private def distanceToGhosts(model: Model): Int = model.ghosts.foldLeft(0) { case (acc, g) =>
        val p = model.pacman
        acc + (p.x - g.x).abs + (p.y - g.y).abs
    }
}
