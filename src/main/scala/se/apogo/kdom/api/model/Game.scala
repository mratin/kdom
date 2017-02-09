package se.apogo.kdom.api.model

import se.apogo.kdom.KingdomScorer
import se.apogo.kdom.state.GameState

case class Tile(terrain: String, crowns: Int) extends JsonSerializable

case class Domino(number: Int, tile1: Tile, tile2: Tile) extends JsonSerializable

case class DraftDomino(player: Option[Player], domino: Domino) extends JsonSerializable

case class Draft(dominoes: Seq[DraftDomino]) extends JsonSerializable

case class Player(name: String) extends JsonSerializable
case class PlayerWithToken(name: String, uuid: String, callBackUrl: Option[String]) extends JsonSerializable

case class Position(row: Int, col: Int)

case class PlacedTile(position: Position, tile: Tile)

case class Score(total: Int, areaScores: Seq[Int], centerBonus: Int, completeBonus: Int)

case class Kingdom(player: Player, placedTiles: Seq[PlacedTile], score: Score) extends JsonSerializable

case class Game(uuid: String,
                created: String,
                updated: String,
                kingdoms: Seq[Kingdom],
                currentDraft: Draft, previousDraft: Draft,
                currentPlayer: Option[Player],
                gameOver: Boolean,
                turn: Int)
  extends JsonSerializable {
}

case class PlacedDomino(domino: Domino, tile1Position: Position, tile2Position: Position) extends JsonSerializable

case class Move(number: Int, chosenDomino: Option[Domino], placedDomino: Option[PlacedDomino]) extends JsonSerializable

case class NewGame(uuid: String, created: String, updated: String, numberOfPlayers: Int, joinedPlayers: Seq[Player]) extends JsonSerializable

case class NewGames(newGames: Seq[NewGame]) extends JsonSerializable

case class BriefGame(uuid: String, created: String, updated: String, players: Seq[Player],
                     playerOnTurn: Option[Player], round: Int, turn: Int, gameOver: Boolean) extends JsonSerializable

case class BriefGames(games: Seq[BriefGame]) extends JsonSerializable
case class Moves(moves: Seq[Move]) extends JsonSerializable

class ModelConverter() {
  def toBriefRepresentation(gameState: GameState): BriefGame = {
    BriefGame(gameState.uuid.toString,
      created = gameState.created.toString, updated = gameState.updated.toString,
      players = gameState.game.players.toSeq.sortBy(_.name).map(toRepresentation),
      playerOnTurn = if (gameState.game.isAwaitingMove) Some(toRepresentation(gameState.game.currentMeeple.owner)) else None,
      round = gameState.game.round,
      turn = gameState.game.turn,
      gameOver = gameState.game.isGameOver
    )
  }

  def toRepresentation(newGame: se.apogo.kdom.state.NewGame): NewGame = {
    NewGame(newGame.uuid.toString, newGame.created.toString, newGame.updated.toString, newGame.numberOfPlayers,
      newGame.joinedPlayers.map(toRepresentation).toSeq.sortBy(_.name))
  }

  def toRepresentation(gameState: GameState): Game = {
    Game(uuid = gameState.uuid.toString,
      created = gameState.created.toString,
      updated = gameState.updated.toString,
      kingdoms = gameState.game.kingdoms.toSeq.sortBy(_.owner.name).map(toRepresentation(new KingdomScorer())),
      currentDraft = toRepresentation(gameState.game.currentDraft),
      previousDraft = toRepresentation(gameState.game.previousDraft),
      currentPlayer = if (gameState.game.isGameOver) None else Some(toRepresentation(gameState.game.currentMeeple.owner)),
      gameOver = gameState.game.isGameOver,
      turn = gameState.game.turn
    )
  }

  def toRepresentation(draft: se.apogo.kdom.Draft): Draft = {
    Draft(draft.dominoesByNumber.map(domino => {
      DraftDomino(draft.meepleOfDomino(domino).map(_.owner).map(toRepresentation), toRepresentation(domino))
    }))
  }

  def toRepresentation(domino: se.apogo.kdom.Domino): Domino = {
    Domino(domino.number, toRepresentation(domino.tile1), toRepresentation(domino.tile2))
  }

  def toRepresentation(kingdomScorer: KingdomScorer)(kingdom: se.apogo.kdom.Kingdom): Kingdom = {
    Kingdom(player = toRepresentation(kingdom.owner),
      placedTiles = kingdom.positions.map(position =>
        PlacedTile(toRepresentation(position), toRepresentation(kingdom.placedTile(position)))).toSeq,
      score = toRepresentation(kingdomScorer.score(kingdom))
    )
  }

  def toRepresentation(score: se.apogo.kdom.Score): Score = {
    Score(total = score.total, areaScores = score.areaScores, centerBonus = score.centerBonus, completeBonus = score.completeBonus)
  }

  def toRepresentation(tile: se.apogo.kdom.Tile): Tile = {
    Tile(toRepresentation(tile.terrain), tile.crowns)
  }

  def toRepresentation(terrain: se.apogo.kdom.Terrain): String = {
    terrain match {
      case se.apogo.kdom.Castle  => "castle"
      case se.apogo.kdom.Field   => "field"
      case se.apogo.kdom.Forest  => "forest"
      case se.apogo.kdom.Water   => "water"
      case se.apogo.kdom.Pasture => "pasture"
      case se.apogo.kdom.Clay    => "clay"
      case se.apogo.kdom.Mine    => "mine"
    }
  }

  def toRepresentation(position: se.apogo.kdom.Position): Position = {
    Position(row = position.row, col = position.col)
  }

  def toRepresentation(player: se.apogo.kdom.Player): Player = {
    Player(player.name)
  }

  def toRepresentation(move: se.apogo.kdom.Move): Move = {
    Move(move.moveNumber, move.chosenDomino.map(toRepresentation), move.placedDomino.map(toRepresentation))
  }

  def toRepresentation(placedDomino: se.apogo.kdom.PlacedDomino): PlacedDomino = {
    PlacedDomino(toRepresentation(placedDomino.domino),
      toRepresentation(placedDomino.tile1Position),
      toRepresentation(placedDomino.tile2Position))
  }
}
