package services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBObject;

import bd.MessageTools;

public class Message {

	public Message(){
		
	}
	
	public static JSONObject addMessage(String key, String content){
		JSONObject rep = new JSONObject();
		
		try {	
			if(bd.UserTools.keyExists(key)){
				if(bd.UserTools.sessionNotExpired(key)){	
					int id = bd.MessageTools.addMessage(key, content);
					rep.put("key", key);
					rep.put("id_message", id);
					rep.put("content", content);
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
		
	public static JSONObject deleteMessage(String key, String idMessage){
		
		JSONObject rep = new JSONObject();
		
		try{
			if(bd.UserTools.keyExists(key)){	
				if(bd.UserTools.sessionNotExpired(key)){
					if(bd.MessageTools.isYourMessage(key,idMessage)){
						
						bd.MessageTools.deleteMessage(key, idMessage);
						rep.put("status", "ok");		
						
					}else{
						rep.put("status", "ko");
						rep.put("error ", "ce n'est pas votre message.");
					}
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
	
	public static JSONObject getMessagesUser(String key, int id_user) {
		JSONArray tab = new JSONArray();
		JSONObject repfinal = new JSONObject();
		int nb_msg = 0;
		int id_msg = 0;
		List<DBObject> listMessages = bd.MessageTools.getMessagesUser(key, id_user);
		
		try {
			if(bd.UserTools.keyExists(key)){
				if(bd.UserTools.sessionNotExpired(key)){
					for(DBObject db : listMessages){
						try {
							JSONObject rep = new JSONObject();
							rep.put("id", id_msg);
							rep.put("id_message", db.get("_id"));
							rep.put("user", db.get("user_id"));
							rep.put("user_login", bd.UserTools.getLoginById((Integer)db.get("user_id")));
							rep.put("date", db.get("date"));
							rep.put("content",db.get("content"));
							rep.put("comments", db.get("comments"));
							rep.put("nb_comments", db.get("nb_comments"));
							rep.put("likes", db.get("likes"));
							rep.put("nbLikes", db.get("nb_likes"));
							
							// Savoir si l'utilisateur a déjà liker le message.
							boolean ilike = false;
													
							if(bd.MessageTools.alreadyLike(key, (int)db.get("_id")) != null) {
								ilike = true;
							}
							
							rep.put("like_by_me", ilike);
																
							id_msg++;
							
							tab.put(rep);
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					repfinal.put("messages", tab);
					repfinal.put("status", "ok");
				}else{
					repfinal.put("error", "Votre session à expirée veuillez vous reconnecter.");
					repfinal.put("status", "ko");
				}
			}else{
				repfinal.put("error", "vous n'etes pas connecté(e).");
				repfinal.put("status", "ko");
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return repfinal;	
	}

	public static JSONObject getMessagesUsers(String key, String query, String id_max, String id_min, String limit){
		
		JSONArray tab = new JSONArray();
		JSONObject repfinal = new JSONObject();
		int nb_msg = 0;
		int id_msg = 0;
		List<DBObject> listMessages = bd.MessageTools.getMessagesUsers(key, query, id_max, id_min, limit);
		int id_user = bd.UserTools.getIdUserByKey(key);
		
		try {
			if(bd.UserTools.keyExists(key)){
				if(bd.UserTools.sessionNotExpired(key)){
					for(DBObject db : listMessages){
						try {
							JSONObject rep = new JSONObject();
							rep.put("id", id_msg);
							rep.put("id_message", db.get("_id"));
							rep.put("user", db.get("user_id"));
							rep.put("user_login", bd.UserTools.getLoginById((Integer)db.get("user_id")));
							rep.put("date", db.get("date"));
							rep.put("content",db.get("content"));
							rep.put("comments", db.get("comments"));
							rep.put("nb_comments", db.get("nb_comments"));
							rep.put("likes", db.get("likes"));
							rep.put("nbLikes", db.get("nb_likes"));
							
							// Savoir si l'utilisateur a déjà liker le message.
							boolean ilike = false;
													
							if(bd.MessageTools.alreadyLike(key, (int)db.get("_id")) != null) {
								ilike = true;
							}
							
							rep.put("like_by_me", ilike);
																
							id_msg++;
														
							tab.put(rep);
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					repfinal.put("messages", tab);
					repfinal.put("status", "ok");
				}else{
					repfinal.put("error", "Votre session à expirée veuillez vous reconnecter.");
					repfinal.put("status", "ko");
				}
			}else{
				repfinal.put("error", "vous n'etes pas connecté(e).");
				repfinal.put("status", "ko");
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return repfinal;	
	}
	
	
	public static JSONObject addLike(String key, String idMessage) {
		
		JSONObject rep = new JSONObject();
		
		try {	
			if(bd.UserTools.keyExists(key)){
				if(bd.UserTools.sessionNotExpired(key)){	
					bd.MessageTools.addLike(key, idMessage);
					rep.put("key", key);
					rep.put("message", idMessage);
					rep.put("status", "ok");
				}else{
					return servicesTools.servicesRefused.servicesRefused("Votre session à expirée veuillez vous reconnecter.",1001);
				}
				
			}else{
				return servicesTools.servicesRefused.servicesRefused("error", 1001);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		return rep;
	}
	
	public static void main(String args[]) {
		//int t [] = {23,22};
		
		//System.out.println(addMessage("k7c7gudvb3u6okblvj8w", "message test"));
		
		//System.out.println(getMessagesUser("xlpaktbqgq83pg9tuxiz", 23));
		
		//System.out.println(getMessagesUser("jhm2ptp8uryr8ttybiec", 23));
	
		//System.out.println(getMessagesUsers("lnw1u3n1ob7rvnqg957t","", "13", "9", "5" ));
	}
	
}