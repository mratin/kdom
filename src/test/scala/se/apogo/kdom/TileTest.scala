package se.apogo.kdom

import org.testng.Assert
import org.testng.annotations.Test

class TileTest {

  @Test
  def test_create(): Unit =
  {
    val t = Tile(Castle, 0)
    Assert.assertTrue(t.terrain == Castle)
  }
}
