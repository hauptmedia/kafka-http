package de.hauptmedia.http

import javax.servlet.http.HttpServletResponse

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization

import scala.xml.Node

object Responders {
  type Responder = HttpServletResponse => Unit
  implicit val formats = DefaultFormats

  case class ContentType(mime: String)
  
  object ContentType { implicit val defaultValue = ContentType("text/html") }
  
  implicit def toXMLResponse(content: Node)(implicit contentType: ContentType): Responder = {
    response =>
      response.setContentType(contentType.mime)
      response.getWriter.write(content.toString)
  }

  implicit def toJsonResponse(content: AnyRef): Responder = {
    response =>
      response.setContentType("application/json")
      response.getWriter.write(
        Serialization.write(content)
      )
  }
  
  implicit def toPlainTextResponse(content: String): Responder = {
    response =>
      response.setContentType("text/plain")
      response.getWriter.write(content)
  }
  
  implicit def toErrorResponse(code: Int): Responder = { _.sendError(code) }
  
  def redirection(url: String): Responder = { 
    response => response.sendRedirect(response.encodeRedirectURL(url))  
  }
}
