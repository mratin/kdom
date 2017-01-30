package se.apogo.kdom

object Draft {
  val empty = Draft(Set.empty, Map())
}
case class Draft(dominoes: Set[Domino], chosen: Map[Meeple, Domino]) {
  require(chosen.values.forall(dominoes.contains) && chosen.values.size <= dominoes.size)

  def dominoesByNumber: Seq[Domino] = dominoes.toSeq.sortBy(_.number)

  def numberOfMeeples: Int = chosenMeeples.size
  def chosenMeeples: Set[Meeple] = chosen.keySet
  def playersWhoHaveChosen: Set[Player] = chosenMeeples.map(_.owner)

  def chosenDominoes: Set[Domino] = chosen.values.toSet
  def freeDominoes: Set[Domino] = dominoes -- chosenDominoes

  def isEmpty: Boolean = dominoes.isEmpty
  def isFull: Boolean = chosenDominoes == dominoes

  def choose(meeple: Meeple, domino: Domino): Draft =
  {
    require(freeDominoes.contains(domino) && !chosenMeeples.contains(meeple))

    copy(chosen = chosen.updated(meeple, domino))
  }

  def nextMeepleToRemove: Option[Meeple] =
  {
    chosenMeeples.toSeq.sortBy(meeple => chosen(meeple).number).headOption
  }

  def nextDominoToRemove: Option[Domino] = nextMeepleToRemove.map(chosen)

  def removeNext: Draft =
  {
    require(freeDominoes.isEmpty && chosenDominoes.nonEmpty)
    val domino = chosen(nextMeepleToRemove.get)

    copy(dominoes - domino, chosen.filterNot(_._2 == domino))
  }

  def meepleOfDomino(domino: Domino): Option[Meeple] = {
    chosenMeeples.find(chosen(_) == domino)
  }
}
