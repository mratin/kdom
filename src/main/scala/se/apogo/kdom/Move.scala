package se.apogo.kdom

case class Move(moveNumber: Int,
                meeple: Meeple,
                chosenDomino: Option[Domino],
                placedDomino: Option[PlacedDomino]) extends Ordered[Move] {
  require(chosenDomino.isDefined || placedDomino.isDefined)
  require(chosenDomino != placedDomino.map(_.domino))

  def player: Player = meeple.owner

  override def compare(that: Move): Int = {
    def chosenNr(m: Move): Int = m.chosenDomino.map(_.number).getOrElse(0)
    def position1(m: Move): Position = m.placedDomino.map(_.tile1Position).getOrElse(Position(0,0))
    def position2(m: Move): Position = m.placedDomino.map(_.tile2Position).getOrElse(Position(0,0))

    val c = chosenNr(this).compareTo(chosenNr(that))
    if (c == 0) {
      val c2: Int = position1(this).compare(position1(that))
      if (c2 == 0) {
        position2(this).compare(position2(that))
      } else {
        c2
      }
    } else {
      c
    }
  }
}
