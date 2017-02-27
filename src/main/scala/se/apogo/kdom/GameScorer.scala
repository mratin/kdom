package se.apogo.kdom

object GameScorer {
  def apply() = new GameScorer(new KingdomScorer)
}
class GameScorer(kingdomScorer: KingdomScorer) {
  def score(game: Game): GameScore = {
    val playerScores: Map[String, Score] =
      game.kingdoms.map(kingdom => kingdom.owner.name -> kingdomScorer.score(kingdom)).toMap

    GameScore(playerScores.toSeq.sortBy(_._2.total).reverse)
  }
}

case class GameScore(orderedPlayerScores: Seq[(String, Score)])
