package se.apogo.kdom

object KingdomPlacementValidator {
  def apply(): KingdomPlacementValidator = {
    new KingdomPlacementValidator(new KingdomSizeValidator())
  }
}
class KingdomPlacementValidator(kingdomSizeValidator: KingdomSizeValidator) {
  def validate(kingdom: Kingdom, placedDomino: PlacedDomino): Boolean =
  {
    val hasOverlap: Boolean =
      kingdom.placedTile.isDefinedAt(placedDomino.tile1Position) ||
      kingdom.placedTile.isDefinedAt(placedDomino.tile2Position)

    val isConnected: Boolean =
      isConnectedToValidTerrain(kingdom, placedDomino.domino.tile1.terrain, placedDomino.tile1Position) ||
      isConnectedToValidTerrain(kingdom, placedDomino.domino.tile2.terrain, placedDomino.tile2Position)

    val hasValidSize: Boolean =
    {
      kingdomSizeValidator.hasValidSize(kingdom.placedTile.keys ++
        Set(placedDomino.tile1Position, placedDomino.tile2Position))
    }

    !hasOverlap && isConnected && hasValidSize
  }

  private def isConnectedToValidTerrain(kingdom: Kingdom, terrain: Terrain, position: Position): Boolean = {
    position.neighbours.
      exists(neighbour => kingdom.placedTile.get(neighbour).
        exists(tile => tile.terrain == terrain || tile.terrain == Castle))
  }
}
