package se.apogo.kdom

import org.testng.Assert
import org.testng.annotations.Test

class AvailableMovesTest {
  @Test
  def test_moves_singleCastle(): Unit = {
    val availableMoves = new AvailablePlacements(new KingdomPlacementValidator(new KingdomSizeValidator(5)))

    val domino = Domino(0, Tile(Field, 1), Tile(Forest, 0))
    val kingdom = Kingdom.start(Player(""))

    val moves = availableMoves.validPlacements(kingdom, domino)

    Assert.assertTrue(moves.size == 24)
  }
}
