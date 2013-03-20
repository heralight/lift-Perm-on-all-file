package code.snippet


/**
 * Under Apache 2 license
 * based on embed.scala from liftweb
 * <creator>Alexandre Richonnier</creator>
 * <creationDate>19/03/13 18:10</creationDate>
 */
 
 import net.liftweb.common._
import net.liftweb.http._
import S._
import net.liftweb.util._
import Helpers._
import scala.xml._
import scala.xml._
import net.liftweb.common._

import Box._
import code.lib.MarkdownParser



object EmbedMD extends DispatchSnippet {
  private lazy val logger = Logger(this.getClass)

  def register {
    LiftRules.snippets.append {
      case List("embedMD")           => EmbedMD.render _
    }
  }

  def dispatch : DispatchIt = {
    case _ => render _
  }
  val suffixes = List("md","markdown")

  /**
   * Based on Lift Template.scala findRawTemplate
   * @param path
   * @return
   */
   def findMarkdownTemplate(path : String) : Box[NodeSeq] = {
     val se = suffixes.iterator
     val sl = List("_" + locale.toString, "_" + locale.getLanguage, "")


     var found = false
     var ret: NodeSeq = null

     while (!found && se.hasNext) {
       val s = se.next
       val le = sl.iterator
       while (!found && le.hasNext) {
         val p = le.next
         val name = path + p + (if (s.length > 0) "." + s else "")
         import scala.xml.dtd.ValidationException
         val xmlb = try {
           LiftRules.loadResourceAsString(name) match {
             case Full(b) =>  MarkdownParser.parse(b)
             case _ => Empty
           }
         } catch {
           case e: ValidationException if Props.devMode | Props.testMode =>
             return Helpers.errorDiv(<div>Error locating template: <b>{name}</b><br/>
               Message: <b>{e.getMessage}</b><br/>
               {
               <pre>{e.toString}{e.getStackTrace.map(_.toString).mkString("\n")}</pre>
               }
             </div>)

           case e: ValidationException => Empty
         }
         if (xmlb.isDefined) {
           found = true
           ret = xmlb.openOrThrowException("passes isDefined")
         } else if (xmlb.isInstanceOf[Failure] &&
           (Props.devMode | Props.testMode)) {
           val msg = xmlb.asInstanceOf[Failure].msg
           val e = xmlb.asInstanceOf[Failure].exception
           return Helpers.errorDiv(<div>Error locating MD template: <b>{name}</b><br/>Message: <b>{msg}</b><br/>{
             {
               e match {
                 case Full(e) =>
                   <pre>{e.toString}{e.getStackTrace.map(_.toString).mkString("\n")}</pre>
                 case _ => NodeSeq.Empty
               }
             }}
           </div>)
         }
       }
     }
     if (found) Full(ret)
     else Empty
   }

  def render(kids: NodeSeq) : NodeSeq =
    (for {
      ctx <- S.session ?~ ("FIX"+"ME: session is invalid")
      what <- S.attr ~ ("what") ?~ ("FIX" + "ME The 'what' attribute not defined. In order to embed a template, the 'what' attribute must be specified")
      templateOpt <- findMarkdownTemplate(what.text) ?~ ("FIX"+"ME trying to embed a template named '"+what+"', but the template not found. ")
    } yield (what, Templates.checkForContentId(templateOpt))) match {
      case Full((what,template)) => {
        val bindingMap : Map[String,NodeSeq] = Map(kids.flatMap({
          case p : scala.xml.PCData => None // Discard whitespace and other non-tag junk
          case t : scala.xml.Text => None // Discard whitespace and other non-tag junk
          case e : Elem if e.prefix == "lift" && e.label == "bind-at" => {
            e.attribute("name") match {
              /* DCB: I was getting a type error if I just tried to use e.child
               * here. I didn't feel like digging to find out why Seq[Node]
               * wouldn't convert to NodeSeq, so I just do it with fromSeq. */
              case Some(name) => Some(name.text -> NodeSeq.fromSeq(e.child))
              case None => logger.warn("Found <lift:bind-at> tag without name while embedding \"%s\"".format(what.text)); None
            }
          }
          case _ => None
        }): _*)

        BindHelpers.bind(bindingMap, template)
      }
      case Failure(msg, _, _) =>
        logger.error("'embed' snippet failed with message: "+msg)
        throw new SnippetExecutionException("Embed Snippet failed: "+msg)

      case _ =>
        logger.error("'embed' snippet failed because it was invoked outside session context")
        throw new SnippetExecutionException("session is invalid")
    }

}

