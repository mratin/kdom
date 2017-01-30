package se.apogo.kdom

object Tile
{
  val CastleTile = Tile(Castle, 0)
}
case class Tile(terrain: Terrain, crowns: Int = 0) {

}

