



import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
//import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class  jettyserver {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Server server = new Server(1122);  
		/*
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[]{"index.html","test4.html","testowl.jpg"});
        
		resource_handler.setResourceBase(".");
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler(), new serverHandler()});
		server.setHandler(handlers);
		*/	
		server.setHandler(new serverHandler());
	
		server.start();
        server.join();
	}
}
/*
public class jettyserver {	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Server server = new Server(1234);   	
		//ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
				//context.setContextPath("/");
				//context.addServlet(new ServletHolder(new HelloServlet()),"/*");
		server.setHandler(new serverHandler());		
        server.start();
        server.join();
	}
}*/