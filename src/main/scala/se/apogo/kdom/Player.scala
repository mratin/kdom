package se.apogo.kdom

import java.util.UUID

object Player {
  def apply(name: String): Player = {
    Player(name, UUID.randomUUID())
  }
}
case class Player(name: String, uuid: UUID)
