package servlet.message;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class GetMessages extends HttpServlet{

	public GetMessages(){
		super();
	}

	protected void doGet(HttpServletRequest requeste, HttpServletResponse response) throws ServletException, IOException{
		
		response.setContentType("Text / Plain");
		
		String key = requeste.getParameter("key");
		String query = requeste.getParameter("query");
		String id_min = requeste.getParameter("id_min");
		String id_max = requeste.getParameter("id_max");
		String limit = requeste.getParameter("nb");
		
		JSONObject obj = new JSONObject();
		
		if(key == null || id_min == null || id_max == null || limit == null){
			
			obj = servicesTools.servicesRefused.servicesRefused("Il manque des parametres", 1001);
			response.getWriter().println(obj.toString());
		}
		else{
			if(query == null)
				query = "";
			
			try{
				obj = services.Message.getMessagesUsers(key, query, id_max, id_min, limit);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		PrintWriter out = response.getWriter();
		out.println(obj.toString());
	}
}
