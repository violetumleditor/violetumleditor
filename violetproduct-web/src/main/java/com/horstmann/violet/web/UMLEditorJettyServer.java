package com.horstmann.violet.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.horstmann.violet.web.util.webapp.BeanFactoryServletContextListener;

import eu.webtoolkit.jwt.ServletInit;

public class UMLEditorJettyServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		ServletContextHandler handler = new ServletContextHandler();
		handler.setContextPath("/");
		
		ServletHolder sh = new ServletHolder(new UMLEditorWebServlet());
		sh.setName("VioletJWT");
		handler.addServlet(sh, "/*");
		handler.addEventListener(new BeanFactoryServletContextListener());
		handler.addEventListener(new ServletInit());
		handler.setSessionHandler(new SessionHandler());
		
		GzipHandler gzipHandler = new GzipHandler();
	    gzipHandler.setIncludedMethods("PUT", "POST", "GET");
	    gzipHandler.setInflateBufferSize(2048);
	    gzipHandler.setHandler(handler);
	    server.setHandler(gzipHandler);
		
		server.start();
		server.join();
	}

}