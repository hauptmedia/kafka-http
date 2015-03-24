package de.hauptmedia.http

import org.mortbay._
import jetty.Server
import jetty.handler.{HandlerCollection, DefaultHandler, AbstractHandler, ContextHandlerCollection}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import log.Log
import org.mortbay.thread.QueuedThreadPool

object Driver {
  val server = new Server

  val contexts = new ContextHandlerCollection
  val handlers = new HandlerCollection
  val threadPool = new QueuedThreadPool(40)

  handlers setHandlers Array(requestLogger, contexts, new DefaultHandler)
  server setHandler handlers
  
  def start = {
    server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 35 * 1024 * 1024)

    for(connector <- server.getConnectors()){
      connector.setRequestBufferSize(35 * 1024 * 1024);
    }

    server.setThreadPool(threadPool)
    //      val context = new ServletContextHandler(ServletContextHandler.SESSIONS)


    server.start
  }

  def join = server.join
  def stop = server.stop
  
  object requestLogger extends AbstractHandler {
    def handle( target: String, request: HttpServletRequest, response: HttpServletResponse, kind: Int) {
      Log debug request.getMethod + " " + target
    }
  }
}
