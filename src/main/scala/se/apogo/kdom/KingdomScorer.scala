package se.apogo.kdom

class KingdomScorer {

  def score(kingdom: Kingdom): Score = {

    val areas: Set[Set[Position]] = kingdom.placedTile.keys.map(findArea(kingdom)).toSet

    assert (areas.toSeq.map(_.size).sum == kingdom.placedTile.keys.size)

    val centerBonus = if (isCastleCentered(kingdom)) 10 else 0
    val completeBonus = if (isKingdomComplete(kingdom)) 5 else 0

    Score(areas.toSeq.map(computeAreaScore(kingdom)), centerBonus = centerBonus, completeBonus = completeBonus)
  }

  private def findArea(kingdom: Kingdom, area: Set[Position] = Set.empty)
                      (position: Position): Set[Position] =
  {
    require(kingdom.placedTile.isDefinedAt(position))

    val currentTile: Tile = kingdom.placedTile(position)

    val scoredTerrain: Terrain = area.map(kingdom.placedTile).headOption.map(_.terrain).getOrElse(currentTile.terrain)

    if (area.contains(position) || scoredTerrain != currentTile.terrain) {
      area
    } else {
      val definedNeighbours = position.neighbours.filter(kingdom.placedTile.isDefinedAt)

      (area + position) ++ definedNeighbours.flatMap(findArea(kingdom, area + position))
    }
  }

  private def computeAreaScore(kingdom: Kingdom) (area: Set[Position]): Int =
  {
    val tiles = area.map(kingdom.placedTile)
    val crowns = tiles.map(_.crowns).sum

    crowns * tiles.size
  }

  def isKingdomComplete(kingdom: Kingdom): Boolean = {
    // TODO support large kingdoms
    kingdom.positions.size == 5 * 5
  }

  def isCastleCentered(kingdom: Kingdom) = {
    // TODO support large kingdoms
    kingdom.positions.forall(p => -2 <= p.col && p.col <= 2 && -2 <= p.row && p.row <= 2)
  }

}


