package code.lib


/**
 * project test, not use in production!!!
 * <creator>Alexandre Richonnier</creator>
 */

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util._
import Helpers._
import code.model.User


object ProxyGuard extends Logger {
  def register = {

    // Lift servlet will process all request on /private
    LiftRules.liftRequest.append {
      case Req("private" :: _, _, _) => true
    }

    // Can be transform to a rest service
    LiftRules.dispatch.prepend(NamedPF("File Dispatch") {
      case r@Req("private" :: _, _, _) if canRender(r) && User.loggedIn_? => {
        val res = tryo {
          error("r:" + r.uri + " l: " + getLastSuffix(r))
          // val pls = r.path.partPath.mkString("/", "/", "." + r.path.suffix)
          val pls = r.uri
          LiftRules.loadResource(pls) match {
            case Full(b) => FileResponse(b, Nil, 200, MimeTypes.getMimeType(getLastSuffix(r)))
            case _ => NotFoundResponse()
          }
        }
        () => res
      }
    })

    def getLastSuffix(r: Req): String = {
      val last = r.path.wholePath.last
      last.substring(last.lastIndexOf('.') + 1)
    }

    // filter, can be invert like all != "html"
    def canRender(r: Req): Boolean = {
      getLastSuffix(r) match {
        case "css" => true
        case "js" => true
        case _ => false
      }
    }
  }

  case class FileResponse(b: Array[Byte], headers: List[(String, String)] = Nil, code: Int = 200, contentType: String = "text/html;") extends LiftResponse {
    def toResponse = {
      InMemoryResponse(b, ("Content-Length", b.length.toString) ::("Content-Type", contentType) :: headers, Nil, code)
    }
  }

}

