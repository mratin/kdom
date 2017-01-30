package se.apogo.kdom

trait PositionContainer {
  def positions: Iterable[Position]
}

object Kingdom {
  def start(player: Player): Kingdom = {
    Kingdom(player, Map(Position(0,0) -> Tile.CastleTile))
  }
}

case class Kingdom(owner: Player, placedTile: Map[Position, Tile]) {
  require(placedTile.isDefinedAt(Position(0,0)))

  def positions: Iterable[Position] = placedTile.keys

  def placeDomino(placedDomino: PlacedDomino): Kingdom =
  {
    copy(placedTile =
      placedTile.
        updated(placedDomino.tile1Position, placedDomino.domino.tile1).
        updated(placedDomino.tile2Position, placedDomino.domino.tile2))
  }
}
