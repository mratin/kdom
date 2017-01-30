package se.apogo.kdom

case class PlacedDomino(domino: Domino, tile1Position: Position, tile2Position: Position) {
  require(tile1Position.neighbours.contains(tile2Position))
}
