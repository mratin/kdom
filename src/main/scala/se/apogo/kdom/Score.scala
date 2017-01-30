package se.apogo.kdom

case class Score(areaScores: Seq[Int], centerBonus: Int, completeBonus: Int) {
  def total: Int = areaScores.sum + centerBonus + completeBonus
}
