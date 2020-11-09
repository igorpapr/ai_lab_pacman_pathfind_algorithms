package pacman

case class Matrix[+A](cols: Int, rows: Int, data: Vector[A]) {
    lazy val size: Int = rows * cols

    def apply(col: Int, row: Int): A = data(row * (rows) + col)
    def apply(cr: (Int, Int)): A = this(cr._1, cr._2)

    def updated[B >: A](col: Int, row: Int, elem: B): Matrix[B] = copy(data = data.updated(row * (rows) + col, elem))
}
