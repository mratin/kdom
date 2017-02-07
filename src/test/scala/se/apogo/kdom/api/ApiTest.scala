package se.apogo.kdom.api

import io.shaka.http.Http.http
import io.shaka.http.Request.{GET, POST}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.testng.annotations.Test
import model._

import scala.util.Random

class ApiTest {
  val apiUrl = "http://localhost:8080"

  implicit val formats = DefaultFormats

  @Test(enabled = false)
  def test_playGame(): Unit = {
    val response = http(POST(apiUrl + "/new-games/"))

    val json: JValue = parse(response.entityAsString)

    val newGame: NewGame = json.extract[NewGame]

    val gameId: String = newGame.uuid

    val playerNames = Seq("A", "B", "C", "D")

    val joinedPlayers: Seq[PlayerWithToken] = {
      for {
        playerName <- playerNames
        response = http(POST(s"${apiUrl}/new-games/${gameId}/join/${playerName}"))
      } yield {
        parse(response.entityAsString).extract[PlayerWithToken]
      }
    }

    def playGame(): Game = {
      val game: Game = parse(http(GET(s"${apiUrl}/games/${gameId}")).entityAsString).extract[Game]
      if (game.gameOver) {
        game
      } else {
        val availableMoves: Moves = parse(http(GET(s"${apiUrl}/games/${gameId}/available-moves")).entityAsString).extract[Moves]

        val chosenMove: Move = availableMoves.moves(Random.nextInt(availableMoves.moves.size))

        val currentPlayerToken = joinedPlayers.find(_.name == game.currentPlayer.get.name).head

        http(POST(s"${apiUrl}/games/${gameId}/players/${currentPlayerToken.uuid}/moves/${chosenMove.number}"))
        playGame()
      }
    }

    val endGame: Game = playGame()

    print(response)
  }
}
