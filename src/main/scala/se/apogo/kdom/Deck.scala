package se.apogo.kdom

import scala.util.Random

object Deck {
  def newDeck(seed: Long, numberOfDominoes: Int): Deck = {
    require(numberOfDominoes <= 48)
    Deck(new Random(seed).shuffle(Domino.allDominoes).take(numberOfDominoes))
  }

}
case class Deck(dominoes: Seq[Domino]) {
  require(dominoes.distinct == dominoes)

  def draft(n: Int): (Deck, Draft) = {
    require(dominoes.size >= n)

    (Deck(dominoes.drop(n)), Draft(dominoes.take(n).toSet, Map()))
  }

  def isEmpty: Boolean = dominoes.isEmpty
}

