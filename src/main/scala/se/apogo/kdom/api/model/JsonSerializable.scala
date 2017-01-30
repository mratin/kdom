package se.apogo.kdom.api.model

import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization

trait JsonSerializable {
  import org.json4s.native.JsonMethods._
  implicit val formats = Serialization.formats(NoTypeHints)
  def toJson: String = {
    val json = Serialization.writePretty(this)
    pretty(render(parse(json)))
  }
}

case class JsonString(string: String) extends JsonSerializable {
  override def toJson: String = string
}
