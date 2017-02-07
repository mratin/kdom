package se.apogo.kdom

import java.util.UUID

object Player {
  def apply(name: String, callbackUrl: Option[String]): Player = {
    apply(name).copy(callbackUrl = callbackUrl)
  }
  def apply(name: String): Player = {
    Player(name, UUID.randomUUID(), None)
  }
}
case class Player(name: String, uuid: UUID, callbackUrl: Option[String])
