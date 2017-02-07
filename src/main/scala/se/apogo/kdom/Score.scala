package se.apogo.kdom

case class Score(areaScores: Seq[Int], centerBonus: Int, completeBonus: Int) {
  def total: Int = areaScores.sum + centerBonus + completeBonus
  override def toString: String = s"Total: ${total} (${areaScores.mkString(",")}) (Center: ${centerBonus} Complete: ${completeBonus})"
}
