/**
 * 
 */
package it.fabryprog.mysql.statistics;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author fabryprog <fabryprog@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class StatsServlet extends HttpServlet {

	//URI /{token} example: /ANDHWO
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		this.doit(request, response, false);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		//this.doit(request, response, true);
	}
	
	protected void doit(HttpServletRequest request,
			HttpServletResponse response, boolean isPostReq) throws ServletException,
			IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String pathInfo = request.getPathInfo();

		System.out.println("Invoked: " + pathInfo);
		
		String[] pathArray = pathInfo.split("/");
		
		if(pathArray.length > 0) {
			execute(request, response, isPostReq, pathArray);
		}
	}

	private void execute(HttpServletRequest request,
			HttpServletResponse response, boolean isPostReq, String[] pathArray)
			throws IOException{
		String token = pathArray[1];
		if(token != null) {
			try {
				
				List output = MySqlConnector.getInstance().executeToken(token, StandAlone.prop, Collections.EMPTY_LIST);
					
				//parsing to JSON
				ObjectMapper mapper = new ObjectMapper();
				response.getWriter().println(mapper.writeValueAsString(output));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
//				try {conn.close();} catch(Exception e) {}
			}
		} else {
		}
	}
}