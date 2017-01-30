package se.apogo.kdom

import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.testng.Assert
import org.testng.annotations.Test


class KingdomPlacementValidatorTest {
  @Test
  def test_placeNextToCastle_true(): Unit =
  {
    val kingdom = Kingdom.start(Player(""))

    val kingdomSizeValidator = mock(classOf[KingdomSizeValidator])
    when(kingdomSizeValidator.hasValidSize(ArgumentMatchers.any())).thenReturn(true)

    val domino = Domino(0, Tile(Field), Tile(Water))
    Assert.assertTrue(new KingdomPlacementValidator(kingdomSizeValidator).validate(kingdom, PlacedDomino(domino, Position(0,1), Position(0,2))))
  }
}
