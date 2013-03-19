package code.lib

import net.liftweb.util._
import net.liftweb.util.Helpers._
import java.util.{ InvalidPropertiesFormatException, Properties}
import java.io.{InputStream, ByteArrayInputStream}
import net.liftweb.common._


object PropsUtils extends Logger {
  def getOrDie(str: String) = {
    Props.get(str) getOrElse (throw new Exception("The following required property is not defined: " + str))
  }

  def getOrDie(props: Properties, str: String) = {
    Box.legacyNullTest[String](props.getProperty(str)) getOrElse (
      throw new Exception("The following required property is not defined: " + str))
  }

  def get(props: Properties, str: String) = {
    Box.legacyNullTest[String](props.getProperty(str))
  }

  /**
   * Get Properties from a property file
   * @param path file path e.g.: "/ddd.props" search in /resources
   */
  def getProperties(path: String): Box[Properties] = {

    def getInput(): Box[InputStream] = {
      val res = tryo {
        getClass.getResourceAsStream(path)
      }.filter(_ ne null)
      trace("Trying to open resource %s. Result=%s".format(path, res))
      res
    }

    getInput() match {
      case Full(x) => {
        val ret = new Properties
        val ba = Helpers.readWholeStream(x)
        try {
          ret.loadFromXML(new ByteArrayInputStream(ba))
          debug("Loaded XML properties from resource %s".format(path))
        } catch {
          case _: InvalidPropertiesFormatException =>
            ret.load(new ByteArrayInputStream(ba))
            debug("Loaded key/value properties from resource %s".format(path))
        }
        Full(ret)
      }
      case _ => error("cannot load property file for resource: " + path)
      Empty
    }
  }
}