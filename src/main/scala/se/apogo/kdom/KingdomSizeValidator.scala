package se.apogo.kdom

class KingdomSizeValidator(maxDimension: Int = 5) {
  def hasValidSize(positions: Iterable[Position]): Boolean =
  {
    positions.nonEmpty && {

      val minRow = positions.map(_.row).min
      val maxRow = positions.map(_.row).max
      val minCol = positions.map(_.col).min
      val maxCol = positions.map(_.col).min

      maxRow - minRow <= maxDimension && maxCol - minCol <= maxDimension
    }
  }
}

