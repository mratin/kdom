package se.apogo.kdom

class GameEngine(availablePlacements: AvailablePlacements) {
  def availableMoves(game: Game): Seq[Move] =
  {
    if (!game.isAwaitingMove) {
      Nil
    } else {
      val meeple = game.currentMeeple
      val kingdom = game.playerKingdom(meeple.owner)

      val freeDominoes: Set[Domino] = game.currentDraft.freeDominoes

      val dominoToPlace: Option[Domino] = game.nextDominoToRemove

      val placements: Option[Set[PlacedDomino]] = dominoToPlace.map(availablePlacements.validPlacements(kingdom, _)).filter(_.nonEmpty)

      val dominoPlacements: Set[Option[PlacedDomino]] =
      {
        placements match {
          case Some(validPlacements) => validPlacements.map(Some(_))
          case None => Set(None)
        }
      }

      val chosenDominoes: Set[Option[Domino]] =
      {
        if (freeDominoes.isEmpty) Set(None) else freeDominoes.map(Some(_))
      }

      (for {
        chosenDomino: Option[Domino]       <- chosenDominoes
        placement: Option[PlacedDomino]    <- dominoPlacements
      } yield {
        Move(0, meeple, chosenDomino, placement)
      }).toSeq.sorted.zipWithIndex.map(mi => mi._1.copy(moveNumber = mi._2))
    }
  }
}
