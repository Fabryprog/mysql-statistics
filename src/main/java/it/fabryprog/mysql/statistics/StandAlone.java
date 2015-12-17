/**
 * 
 */
package it.fabryprog.mysql.statistics;

import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * @author fabryprog <fabryprog@gmail.com>
 *
 */
public class StandAlone {

	public static Properties prop;
	public void init() throws Exception {
		MySqlConnector.getInstance().connect();
		MySqlConnector.getInstance().enableLog();
	}
	
	public void close() throws Exception {
		MySqlConnector.getInstance().disableLog();
	}
	
	public void run() throws Exception {
		
		Server server = new Server(Integer.valueOf(prop
				.getProperty("web.port")));

		ServletHandler handler = new ServletHandler();

		handler.addServletWithMapping(StatsServlet.class, "/api/*");
		
		ResourceHandler staticResourceHandler= new ResourceHandler();
        staticResourceHandler.setResourceBase("web");
        staticResourceHandler.setDirectoriesListed(false);

        ContextHandler staticContextHandler= new ContextHandler("/");
        staticContextHandler.setHandler(staticResourceHandler);
        
        
        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[] { staticContextHandler, handler });
        
        server.setHandler(handlerList);
		server.start();
		server.join();
	}

	public static void main(String[] args) throws Exception {
		final String configDefault = "/config.properties";
		prop = new Properties();
		prop.load(StandAlone.class.getResourceAsStream(configDefault));

		StandAlone instance = null;
		try {
			instance = new StandAlone();
			instance.init();
			instance.run();
		} finally {
			instance.close();
		}
		
	}
}
