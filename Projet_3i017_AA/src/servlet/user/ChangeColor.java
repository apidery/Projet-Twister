package servlet.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeColor extends HttpServlet {

	public ChangeColor(){
		super();
	}
	
	public void doGet(HttpServletRequest requeste, HttpServletResponse response) throws ServletException, IOException{
		
		response.setContentType("Text / Plain");
		
		String color = requeste.getParameter("color");
		String key = requeste.getParameter("key");
		
		JSONObject ret = new JSONObject();
		
		if(color == null || key == null){
			ret = servicesTools.servicesRefused.servicesRefused("Il manque des parametres", 1001);
			response.getWriter().println(ret.toString());
		}
		else{
			try{
				ret = services.User.changeColor(key,color);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			PrintWriter out = response.getWriter();
			out.println(ret.toString());
		}
		
	}
}
