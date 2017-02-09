package se.apogo.kdom

import org.testng.Assert
import org.testng.annotations.Test

class KingdomScorerTest {

  @Test
  def test_singleCastle_0(): Unit =
  {
    val kingdom = Kingdom.start(Player(""))

    Assert.assertTrue(new KingdomScorer().score(kingdom) == Score(Seq(0), 10, 0))
  }

  @Test
  def test_singleCastleAndSingleFieldWithCrown_01(): Unit =
  {
    val kingdom = Kingdom(Player(""), Map[Position, Tile](
      Position(0,0) -> Tile.CastleTile,
      Position(0,1) -> Tile(Field, 1)
    ))

    val score = new KingdomScorer().score(kingdom)

    Assert.assertTrue(score.total == 11)
    Assert.assertTrue(score.areaScores.size == 2)
  }

  @Test
  def test_adjacentFieldsWith1CrownTotal_12(): Unit =
  {
    val kingdom = Kingdom(Player(""), Map[Position, Tile](
      Position(0,0) -> Tile(Field, 0),
      Position(0,1) -> Tile(Field, 1)
    ))

    val score = new KingdomScorer().score(kingdom)

    Assert.assertTrue(score.total == 12)
    Assert.assertTrue(score.areaScores.size == 1)
  }

  @Test
  def test_adjacentEqualFieldsWith1Crown_14(): Unit =
  {
    val kingdom = Kingdom(Player(""), Map[Position, Tile](
      Position(0,0) -> Tile(Field, 1),
      Position(0,1) -> Tile(Field, 1)
    ))

    val score = new KingdomScorer().score(kingdom)

    Assert.assertTrue(score.total == 14)
    Assert.assertTrue(score.areaScores.size == 1)
  }

  @Test
  def test_distinctFieldsWith1CrownEach_101(): Unit =
  {
    val kingdom = Kingdom(Player(""), Map[Position, Tile](
      Position(0,0) -> Tile.CastleTile,
      Position(0,1) -> Tile(Field, 1),
      Position(0,-1) -> Tile(Field, 1)
    ))

    val score = new KingdomScorer().score(kingdom)

    Assert.assertTrue(score.total == 12)
    Assert.assertTrue(score.areaScores.size == 3)
  }
}
