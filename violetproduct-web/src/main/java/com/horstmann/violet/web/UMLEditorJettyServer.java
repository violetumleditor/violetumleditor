package com.horstmann.violet.web;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class UMLEditorJettyServer {

	public static void main(String[] args) throws Exception {
		int port = 8080;
		Server server = new Server(port);
		WebAppContext context = new WebAppContext();
		context.setServer(server);
	    context.setContextPath("/");
	    ProtectionDomain protectionDomain = UMLEditorJettyServer.class.getProtectionDomain();
	    URL location = protectionDomain.getCodeSource().getLocation();
	    //context.setWar(location.toExternalForm());	    
	    // A WEB-INF/web.xml is required for Servlet 3.0
	    context.setResourceBase(location.toExternalForm());
		context.setDescriptor(location.toExternalForm() + "WEB-INF/web.xml");
		// Initialize the various configurations required to auto-wire up
		// the Servlet 3.0 annotations, descriptors, and fragments
		// Specify the context path that you want this webapp to show up as
		context.setContextPath("/");
		// Tell the classloader to use the "server" classpath over the
		// webapp classpath. (this is so that jars and libs in your
		// server classpath are used, requiring no WEB-INF/lib
		// directory to exist)
		context.setParentLoaderPriority(true);
		// Add this webapp to the server
		server.setHandler(context);
		// Start the server thread
		server.start();
		// Wait for the server thread to stop (optional)
		server.join();
//		
//		
//		Server server = new Server();
//	    SocketConnector connector = new SocketConnector();
//	 
//	    // Set some timeout options to make debugging easier.
//	    connector.setMaxIdleTime(1000 * 60 * 60);
//	    connector.setSoLingerTime(-1);
//	    connector.setPort(8080);
//	    server.setConnectors(new Connector[]{connector});
//	 
//	    WebAppContext context = new WebAppContext();
//	    context.setServer(server);
//	    context.setContextPath("/");
//	 
//	    ProtectionDomain protectionDomain = UMLEditorJettyServer.class.getProtectionDomain();
//	    URL location = protectionDomain.getCodeSource().getLocation();
//	    context.setWar(location.toExternalForm());
//	 
//	    server.addHandler(context);
//	    try {
//	        server.start();
//	        System.in.read();
//	        server.stop();
//	        server.join();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        System.exit(100);
//	    }		
	}

}
