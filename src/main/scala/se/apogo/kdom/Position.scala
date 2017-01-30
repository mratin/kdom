package se.apogo.kdom

case class Position(row: Int, col: Int) extends Ordered[Position] {
  def neighbours: Set[Position] = {
    Set(copy(row = row+1), copy(col = col+1), copy(row = row-1), copy(col = col-1))
  }

  override def compare(that: Position): Int = {
    val c = row.compareTo(that.row)
    if (c == 0) {
      col.compareTo(that.col)
    } else {
      c
    }
  }
}
