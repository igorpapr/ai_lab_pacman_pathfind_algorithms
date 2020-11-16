import pacman.Direction.Directions
import pacman._

import scala.collection.mutable
import scala.util.Random

object MiniMax {
    val MinValue: Double = -10000
    val MaxValue: Double = 10000
    var iteration = 0
    var models: mutable.HashSet[Model] = mutable.HashSet()
    val random = new Random()

    def alphabeta(model: Model, depth: Int): (Double, List[Model]) =
        alphabeta(model, depth, 0, MinValue, MaxValue, Nil)

    def alphabeta(model: Model, depth: Int, index: Int): (Double, List[Model]) =
        alphabeta(model, depth, index, MinValue, MaxValue, Nil)

    def alphabeta(m: Model, depth: Int, index: Int, alpha: Double, beta: Double, log: List[Model]): (Double, List[Model]) = {
        //        iteration += 1
        //        models.add(model)
        //        println(s"iteration: $iteration; log len: ${log.length}; unique models: ${models.size};" +
        //          s"model heuristic: ${modelHeuristic(model)}")

        val model = m.eatCandy
        if (depth == 0 || m.candyUnderPacman)
            return (modelCost(m, log), log :+ model)
        if (model.state != ModelState.GoingOn)
            return (modelCost(model, log), log :+ model)
        index match {
            case 0 =>
                var a = alpha
                var value: (Double, List[Model]) = MinValue -> Nil

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
                var value: (Double, List[Model]) = MaxValue -> Nil

                for (child <- possibleMoves(model, index)) {
                    value = Seq(value, alphabeta(
                        child,
                        depth - 1,
                        (index + 1) % (model.ghosts.length + 1),
                        alpha, b,
                        log)).minBy(_._1)
                    b = math.min(b, value._1)
                    if (b <= alpha) return value
                }
                value
        }
    }

    private def possibleMoves(model: Model, index: Int): LazyList[Model] = {
        val res = Directions.flatMap(d => model.moveEntity(index, d))
        if (index > 0 && random.nextBoolean()) random.shuffle(res).take(1)
        else
            res
    }

    private def modelCost(model: Model, log: List[Model]): Double = model.state match {
        case ModelState.GoingOn => goingOnModelCost(model) - log.length
        case ModelState.Win => MaxValue - distanceToGhostsMinSum(model)._2 - log.length
        case ModelState.Lose => MinValue - distanceToGhostsMinSum(model)._2 - log.length
    }

    private def goingOnModelCost(model: Model): Double = {
        val dc = distanceToCandyMin(model)
        val (dgm, dgs) = distanceToGhostsMinSum(model)
        if (dc < dgm)
            dgs - dc*2 - model.candiesCount
        else
            dgm*3 - dc
    }

    private def distanceToGhostsMinSum(model: Model): (Double, Double) = {
        import math.{pow, sqrt}
        val p = model.pacman
        val gds = model.ghosts.map { g => sqrt(pow(p.x - g.x, 2) + pow(p.y - g.y, 2)) }
        (gds.min, gds.sum)
    }

    private def distanceToCandyMin(model: Model): Double = {
        import math.{pow, sqrt}
        if (model.candiesCount == 0) return 1
        val (x, y) = MovingEntity.unapply(model.pacman)
        val candies = for {
            i <- 0 until model.desk.cols
            j <- 0 until model.desk.rows
            cell = model.desk(i, j)
            if cell == Cell.Candy
        } yield (i, j)
        candies.map { case (i, j) => sqrt(pow(x - i, 2) + pow(y - j, 2)) }.min
    }
}
