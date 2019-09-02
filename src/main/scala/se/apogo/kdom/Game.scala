package se.apogo.kdom

import scala.util.Random

object Game {
  def newGame(players: Set[Player], seed: Long): Game = {
    newGame(players, seed, true)
  }

  def newGame(players: Set[Player], seed: Long, shufflePlayers: Boolean) = {
    require(2 <= players.size && players.size <= 4)

    val random: Random = new Random(seed)

    val (meeplesPerPlayer, deckSize): (Int, Int) = {
      players.size match {
        case 2 => (2, 24)
        case 3 => (1, 36)
        case 4 => (1, 48)
      }
    }

    def playerMeeples(player: Player): Set[Meeple] = {
      (for (i <- 1 to meeplesPerPlayer) yield Meeple(i, player)).toSet
    }

    val meeples: Seq[Meeple] = {
      val meeples = players.flatMap(playerMeeples).toSeq
      if (shufflePlayers) {
        random.shuffle(meeples.sortBy(_.owner.name))
      } else {
        meeples
      }
    }
    val numberOfMeeples: Int = meeples.size

    val kingdoms: Set[Kingdom] = players.map(Kingdom.start)

    val (deck, currentDraft): (Deck, Draft) = Deck.newDeck(seed, deckSize).draft(numberOfMeeples)

    Game(kingdoms, deck, currentDraft, Draft.empty, Nil, meeples, Nil, turn = 0).nextTurn
  }
}
case class Game(kingdoms: Set[Kingdom], deck: Deck, currentDraft: Draft, previousDraft: Draft,
                usedDominoes: Seq[Domino], meeplesNotYetInPlay: Seq[Meeple], history: Seq[Game], turn: Int) {

  require(meeplesNotYetInPlay.forall(meeple => players.contains(meeple.owner)))

  if (isAwaitingMove) require((isFirstRound && meeplesNotYetInPlay.nonEmpty) || !isFirstRound && meeplesNotYetInPlay.isEmpty)

  def players: Set[Player] = kingdoms.map(_.owner)

  def numberOfMeeples: Int = {
    players.size match {
      case 2 => 4
      case 3 => 3
      case 4 => 4
    }
  }

  def gameAtTurn(turnNumber: Int): Option[Game] = {
    if (turn == turnNumber) {
      Some(this)
    } else {
      history.find(_.turn == turnNumber)
    }
  }

  def meeples: Set[Meeple] = {
    meeplesNotYetInPlay.toSet ++ previousDraft.chosenMeeples ++ currentDraft.chosenMeeples
  }

  def isAwaitingMove: Boolean = {
    !isGameOver && {
      !(currentDraft.isFull && previousDraft.isEmpty)
    }
  }

  def isGameOver: Boolean = currentDraft.isEmpty && previousDraft.isEmpty

  def round: Int = 1 + (history.size / numberOfMeeples)

  def playerKingdom(player: Player): Kingdom = {
    require(players.contains(player))
    kingdoms.find(_.owner == player).get
  }

  def isFirstRound: Boolean = meeplesNotYetInPlay.nonEmpty
  def isLastRound: Boolean = currentDraft.isEmpty

  /**
    * @return the next meeple to be removed from the previous draft (or place on current draft if first round)
    */
  def currentMeeple: Meeple = {
    require(isAwaitingMove)

    if (isFirstRound) {
      meeplesNotYetInPlay.head
    } else {
      previousDraft.nextMeepleToRemove.get
    }
  }

  /**
    * @return the next domino to be removed (and placed if possible) from the previous draft
    */
  def nextDominoToRemove: Option[Domino] = previousDraft.nextDominoToRemove

  def makeMove(move: Move): Game =
  {
    require(isAwaitingMove)

    if (!isLastRound) require(move.chosenDomino.isDefined)
    if (isFirstRound) require(move.placedDomino.isEmpty)
    if (!isFirstRound) require(move.placedDomino.forall(_.domino == nextDominoToRemove.get))

    val kingdom: Kingdom = playerKingdom(move.meeple.owner)

    // During the first round, no domino is placed in the kingdoms.
    // No domino is placed if it isn't possible to place it
    val newKingdom: Kingdom = move.placedDomino.map(kingdom.placeDomino).getOrElse(kingdom)

    val previousDraftAfterMove: Draft = if (!previousDraft.isEmpty) previousDraft.removeNext else Draft.empty

    // During the last round, no dominoes are chosen in the current draft
    val currentDraftAfterMove: Draft = move.chosenDomino.map(chosenDomino => currentDraft.choose(move.meeple, chosenDomino)).getOrElse(Draft.empty)

    val usedDominoesAfterMove: Seq[Domino] = usedDominoes ++ move.placedDomino.map(_.domino).toSeq

    val gameAfterMove: Game = copy(
      kingdoms = (kingdoms - kingdom) + newKingdom,
      currentDraft = currentDraftAfterMove,
      previousDraft = previousDraftAfterMove,
      usedDominoes = usedDominoesAfterMove,
      meeplesNotYetInPlay = meeplesNotYetInPlay.drop(1)
    )

    if (gameAfterMove.isAwaitingMove || gameAfterMove.isGameOver) {
      gameAfterMove.nextTurn
    } else {
      gameAfterMove.prepareNextRound.nextTurn
    }
  }

  private def nextTurn: Game = {
    copy(
      history = history :+ this,
      turn = turn + 1
    )
  }

  def prepareNextRound: Game =
  {
    require(!isAwaitingMove && !isGameOver)

    val (newDeck, newCurrentDraft) = {
      if (deck.isEmpty) {
        // Last round:
        (deck, Draft.empty)
      } else {
        deck.draft(numberOfMeeples)
      }
    }

    copy(
      deck = newDeck,
      currentDraft = newCurrentDraft,
      previousDraft = currentDraft
    )
  }
}
