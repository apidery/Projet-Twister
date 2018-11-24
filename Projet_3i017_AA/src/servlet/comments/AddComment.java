package servlet.comments;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class AddComment extends HttpServlet{
	
	protected void doGet(HttpServletRequest requeste, HttpServletResponse response) throws ServletException, IOException{
			
		response.setContentType("Text / Plain");
		
		String key = requeste.getParameter("key");
		String content = requeste.getParameter("content");
		String idMessage = requeste.getParameter("idmessage");
		
		JSONObject obj = new JSONObject();
		
		if(key == null || content == null || idMessage == null){
			obj = servicesTools.servicesRefused.servicesRefused("Il manque des parametres", 1001);
			response.getWriter().println(obj.toString());
		}
		else{
			try{
				obj = services.Comments.addComment(key, content, idMessage);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		PrintWriter out = response.getWriter();
		out.println(obj.toString());
	}
}
