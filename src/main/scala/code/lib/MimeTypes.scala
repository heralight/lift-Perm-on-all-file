package code.lib


/**
 * Heirko project
 * <creator>Alexandre Richonnier</creator>
 * <creationDate>20/03/13 15:53</creationDate>
 */

import net.liftweb.util._
import Helpers._

object MimeTypes {
  val defaultMimeType = "application/octet-stream"
  private val mimeTypes = {
    import scala.collection.JavaConversions._
    val props = PropsUtils.getProperties("/props/mime.properties") openOrThrowException ("Missing correct /props/mime.properties")
    props.stringPropertyNames().map {
      case m => {
        (m -> PropsUtils.get(props, m).openOr(defaultMimeType))
      }
    }.toMap
  }


  def getMimeType(suffix: String): String = {
    mimeTypes.get(suffix).getOrElse(defaultMimeType)
  }
}