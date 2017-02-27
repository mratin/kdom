package se.apogo.kdom

import io.shaka.http.FormParameter
import io.shaka.http.Http.http
import io.shaka.http.Request.POST
import se.apogo.kdom.state.GameState

object SlackBot {
  def fromEnv: Option[GameListener] = {
    for {
      slackToken   <- sys.env.get("SLACK_TOKEN")
      slackChannel <- sys.env.get("SLACK_CHANNEL")
      slackUser    <- sys.env.get("SLACK_USER")
      kdomApiUrl   <- sys.env.get("KDOM_API_URL")
    } yield {
      new SlackBot(
        slackToken = slackToken,
        slackChannel = slackChannel,
        slackUser = slackUser,
        kdomApiUrl = kdomApiUrl,
        gameScorer = GameScorer())
    }
  }
}

class SlackBot(slackToken: String,
               slackChannel: String,
               slackUser: String,
               kdomApiUrl: String,
               gameScorer: GameScorer) extends GameListener {

  val slackApiUrl = "https://slack.com/api/"
  def methodUrl(method: String): String = slackApiUrl + method

  def gameUrl(gameId: String): String = {
    s"$kdomApiUrl/games/$gameId"
  }

  def postMessage(message: String): Unit = {
    http(POST(methodUrl("chat.postMessage")).
      formParameters(
        FormParameter("token", slackToken),
        FormParameter("channel", slackChannel),
        FormParameter("as_user", slackUser),
        FormParameter("text", message)
      ))
  }

  override def gameUpdated(gameState: GameState): Unit = {
    if (gameState.game.isGameOver) {
      val report: String = createReport(gameState)
      postMessage(report)
    }
  }

  def createReport(gameState: GameState): String = {

    val gameScore = gameScorer.score(gameState.game)

    val playerMaxLength = gameState.game.players.map(_.name.size).max

    def playerScore(playerScore: (String, Score)): String = {
      val fill: String = Seq.fill(playerMaxLength - playerScore._1.size)(" ").mkString

      s"${playerScore._1}:$fill ${playerScore._2.total}p"
    }

    s"Game finished: ${gameUrl(gameState.uuid.toString)}\n\n```${gameScore.orderedPlayerScores.map(playerScore).mkString("\n")}```"
  }
}
