package se.apogo.kdom.api

import io.shaka.http.Http.http
import io.shaka.http.Request.{GET, POST}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.testng.annotations.Test
import model._

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

        val chosenMove: Move = availableMoves.moves.min(movePriority)

        val currentPlayerToken = joinedPlayers.find(_.name == game.currentPlayer.get.name).head

        http(POST(s"${apiUrl}/games/${gameId}/players/${currentPlayerToken.uuid}/moves/${chosenMove.number}"))
        playGame()
      }
    }

    val endGame: Game = playGame()

    print(response)
  }

  /**
    * @return simple ordering of moves, to get some intelligence when choosing moves
    */
  def movePriority: Ordering[Move] = {
    def positionScalar(position: Position): Int = math.abs(position.col) + math.abs(position.row)
    def placedScalar(placedDomino: PlacedDomino): Int = {
      positionScalar(placedDomino.tile1Position) + positionScalar(placedDomino.tile2Position)
    }
    def crowns(domino: Domino): Int = domino.tile1.crowns + domino.tile2.crowns

    (x: Move, y: Move) => {
      (x.chosenDomino, y.chosenDomino) match {
        case (Some(xChosen), Some(yChosen)) => {

          // Choose the domino with the most crowns:
          val chosenCrownCmp: Int = crowns(xChosen).compareTo(crowns(yChosen))
          if (chosenCrownCmp != 0) chosenCrownCmp else {

            // Choose the domino with the lowest number:
            val chosenNumberCmp: Int = xChosen.number.compareTo(yChosen.number)
            if (chosenNumberCmp != 0) chosenNumberCmp else {

              // Choose the position closest to the castle
              (x.placedDomino, y.placedDomino) match {
                case (Some(xPlaced), Some(yPlaced)) =>
                  placedScalar(xPlaced).compareTo(placedScalar(yPlaced))
                case _ => 0
              }
            }
          }

        }
        case _ => 0
      }
    }
  }

}
