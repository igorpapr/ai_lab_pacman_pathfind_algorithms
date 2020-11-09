package pacman

case class Matrix[+A](rows: Int, cols: Int, data: Vector[A]) {
    lazy val size: Int = rows * cols

    def apply(col: Int, row: Int): A = data(row * rows + col)
    def apply(rc: (Int, Int)): A = this(rc._1, rc._2)

    def updated[B >: A](col: Int, row: Int, elem: B): Matrix[B] = this.copy(data = data.updated(row * rows + col, elem))
}
