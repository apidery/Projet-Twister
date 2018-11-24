package services;

import org.json.JSONException;
import org.json.JSONObject;

public class Comments {
	
	public static JSONObject addComment(String key, String content, String idMessage) {
		JSONObject rep = new JSONObject();
		
		try {	
			if(bd.UserTools.keyExists(key)){
				if(bd.UserTools.sessionNotExpired(key)){	
					bd.CommentTools.addComment(key, content, idMessage);
					rep.put("key", key);
					rep.put("content", content);
					rep.put("message", Integer.parseInt(idMessage));
					rep.put("status", "ok");
				}else{
					rep.put("status", "ko");
					rep.put("error", "Votre session à expirée veuillez vous reconnecter.");
				}
				
				return rep;
			}else{
				return servicesTools.servicesRefused.servicesRefused("error", 1001);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		return rep;
	}
	
	public static JSONObject deleteComment(String key, String idComment, String idMessage) {
		JSONObject rep = new JSONObject();
		
		try{
			if(bd.UserTools.keyExists(key)){	
				if(bd.UserTools.sessionNotExpired(key)){					
					bd.CommentTools.deleteComment(key, idComment, idMessage);
					rep.put("status", "ok");		
				}else{
					rep.put("status", "ko");
					rep.put("error", "Votre session à expirée veuillez vous reconnecter.");
				}
				
			}else{
				return servicesTools.servicesRefused.servicesRefused("error", 1001);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		return rep;
	}
	
	public static JSONObject getAllCommentsForMessage(String idMessage, String key){
		JSONObject rep = new JSONObject();
		
		try{
			if(bd.MessageTools.isYourMessage(key,idMessage)){
				rep = bd.CommentTools.getAllCommentsForMessage(idMessage);
				rep.put("status", "ok");		
				
			}else{
				rep.put("status", "ko");
				rep.put("error ", "ce n'est pas votre message.");
			}
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return rep;
	}

	public static void main(String args[]) {
		System.out.println(addComment("k7c7gudvb3u6okblvj8w", "commentaire 1", "22"));
		//System.out.println(deleteComment("k7c7gudvb3u6okblvj8w", "k7c7gudvb3u6okblvj8w98", "22"));
		System.out.println(getAllCommentsForMessage("22","k7c7gudvb3u6okblvj8w"));
	}
}
