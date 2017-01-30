package se.apogo.kdom

object AvailablePlacements {
  def apply(): AvailablePlacements = {
    new AvailablePlacements(KingdomPlacementValidator())
  }
}
class AvailablePlacements(placementValidator: KingdomPlacementValidator) {
  def validPlacements(kingdom: Kingdom, domino: Domino): Set[PlacedDomino] = {
    for {
      placedPositions <- kingdom.placedTile.keySet
      position1       <- placedPositions.neighbours
      position2       <- position1.neighbours
      placedDomino    <- Set(PlacedDomino(domino, position1, position2), PlacedDomino(domino, position2, position1))
      if placementValidator.validate(kingdom, placedDomino)
    } yield placedDomino
  }
}
