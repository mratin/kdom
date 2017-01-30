package se.apogo.kdom

import org.testng.Assert
import org.testng.annotations.Test

import scala.util.Random

class GameEngineTest {
  @Test
  def test_availableMoves(): Unit = {
    val engine = new GameEngine(AvailablePlacements())
    val game = Game.newGame(Set(Player("A"), Player("B"), Player("C")), seed = 100)

    val availableMoves: Seq[Move] = engine.availableMoves(game)

    Assert.assertTrue(availableMoves.nonEmpty)
  }

  @Test
  def test_playGame(): Unit = {
    val engine = new GameEngine(AvailablePlacements())
    val game = Game.newGame(Set(Player("A"), Player("B"), Player("C")), seed = 105)

    playGame(engine, game, new GreedyScoreMoveChooser(new KingdomScorer))
  }

  private def playGame(engine: GameEngine, game: Game, moveChooser: GreedyScoreMoveChooser): Game =
  {
    if (game.isGameOver)
    {
      // Compute Scores
      println("Game over")
      for (kingdom <- game.kingdoms) {
        val score = new KingdomScorer().score(kingdom)

        println(s"Player ${kingdom.owner.name}, score: ${score}")
      }

      game
    } else {
      val moves = engine.availableMoves(game)

      val move = moveChooser.chooseMove(game, moves)

      println("Performing move: " + move)
      game.makeMove(move)
      playGame(engine, game.makeMove(move), moveChooser)
    }
  }

  class RandomMoveChooser() {

    def chooseMove(moves: Seq[Move]): Move = {
      require(moves.nonEmpty)

      Random.shuffle(moves.toSeq).head
    }
  }

  class GreedyScoreMoveChooser(scorer: KingdomScorer) {
    def chooseMove(game: Game, moves: Seq[Move]): Move = {
      require(moves.nonEmpty)

      moves.maxBy(move => {
        scorer.score(game.makeMove(move).playerKingdom(move.player)).total
      })
    }
  }
}
