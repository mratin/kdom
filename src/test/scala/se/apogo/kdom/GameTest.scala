package se.apogo.kdom

import org.testng.Assert
import org.testng.annotations.Test

class GameTest {
  @Test
  def test_createNewGame(): Unit = {
    val game = Game.newGame(Set(Player("A"), Player("B")), 100)

    Assert.assertTrue(game.numberOfMeeples == 4)
    Assert.assertTrue(game.isAwaitingMove)
    Assert.assertTrue(!game.isGameOver)
    Assert.assertTrue(game.isFirstRound)
    Assert.assertTrue(!game.isLastRound)
  }
}
