package se.apogo.kdom

import se.apogo.kdom.state.GameState

trait GameListener {
  def gameUpdated(gameState: GameState): Unit
}
