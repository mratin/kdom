package se.apogo.kdom.api

import java.util.UUID

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Spark.{before, get, port, post}
import org.eclipse.jetty.http.HttpStatus
import se.apogo.kdom.{AvailablePlacements, GameEngine}
import se.apogo.kdom.api.model._
import se.apogo.kdom.state.{GameState, NewGame, State}
import spark.{Request, Response}

import scala.util.Try

object Main extends App
{
  override def main(args: Array[String])
  {
    val logger: Logger = LoggerFactory.getLogger(getClass)

    val defaultPort = 8080
    val jettyPort: Int = Option(System.getProperties.getProperty("jetty.port")).map(_.toInt).getOrElse(defaultPort)
    port(jettyPort)

    before((request, response) => {
      logger.info(s"Request: ${request.requestMethod} ${request.url}")
    })

    get("/ping", (request, response) => {
      response.`type`("application/json")
      response.status(HttpStatus.OK_200)
      "Server is up"
    })

    ////////////// GAMES ////////////////

    get("/games/:gameId", (request, response) => {
      toJsonWithHttpStatus(response) {
        findGameState(request).map(new ModelConverter().toRepresentation)
      }
    })

    get("/games/", (request, response) => {
      toJsonWithHttpStatus(response) {
        for {
          gameStates <- Some(State.games)
        } yield {
          BriefGames(gameStates.map(new ModelConverter().toBriefRepresentation(_)))
        }
      }
    })

    get("/games/:gameId/available-moves", (request, response) => {
      toJsonWithHttpStatus(response) {
        for {
          gameState <- findGameState(request)
          moves      = new GameEngine(AvailablePlacements()).availableMoves(gameState.game)
        } yield {
          Moves(moves.map(new ModelConverter().toRepresentation))
        }
      }
    })

    post("/games/:gameId/players/:playerToken/moves/:moveNumber", (request, response) => {
      val gameEngine = new GameEngine(AvailablePlacements())

      toJsonWithHttpStatus(response) {
        for {
          gameState      <- findGameState(request)
          uuid           <- Try(UUID.fromString(request.params("playerToken"))).toOption
          moveNumber     <- Try(request.params("moveNumber").toInt).toOption
          if gameState.game.isAwaitingMove
          if gameState.game.currentMeeple.owner.uuid == uuid
          availableMoves = gameEngine.availableMoves(gameState.game)
          move           <- availableMoves.lift(moveNumber)
        } yield {
          assert(move.moveNumber == moveNumber)
          val updatedGame = gameState.game.makeMove(move)
          val updatedGameState = State.updateGame(gameState.uuid, updatedGame)
          new ModelConverter().toRepresentation(updatedGameState)
        }
      }
    })

    ////////////// NEW GAMES ////////////////

    post("/new-games/", (request, response) => {
      toJsonWithHttpStatus(response, onSuccess = HttpStatus.CREATED_201, onFail = HttpStatus.BAD_REQUEST_400) {
        val playerCount: String = Option(request.queryParams("playerCount")).getOrElse("4")
        for {
          numberOfPlayers <- Try(playerCount.toInt).toOption
        } yield {
          val newGame: NewGame =  State.createGame(numberOfPlayers)

          new ModelConverter().toRepresentation(newGame)
        }
      }
    })

    post("/new-games/:gameId/join/:playerName", (request, response) => {
      toJsonWithHttpStatus(response) {
        val callbackUrl: Option[String] = Option(request.queryParams("callbackUrl"))
        for {
          player <- State.joinGame(
            UUID.fromString(request.params("gameId")),
            request.params("playerName"),
            callbackUrl)
        } yield {
          PlayerWithToken(player.name, player.uuid.toString, callbackUrl)
        }
      }
    })

    get("/new-games/", (request, response) => {
      toJsonWithHttpStatus(response) {
        Some(NewGames(State.newGames.map(new ModelConverter().toRepresentation)))
      }
    })
  }

  private def findGameState(request: Request): Option[GameState] = {
    for {
      gameId    <- Option(request.params("gameId"))
      gameState <- State.findGame(UUID.fromString(gameId))
    } yield {
      gameState
    }
  }

  private def toJsonWithHttpStatus(response: Response, onSuccess: Int = HttpStatus.OK_200, onFail: Int = HttpStatus.NOT_FOUND_404)
                                  (result: Option[JsonSerializable]): String = {
    response.`type`("application/json")
    (result match {
      case Some(value) => {
        response.status(onSuccess)
        value
      }
      case None => {
        response.status(onFail)
        JsonString("Not found")
      }
    }).toJson
  }
}
