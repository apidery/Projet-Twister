package bd;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.bson.BSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class CommentTools {
	
	/**
	 * Ajoute un commentaire pour à un message
	 * @param key : clé de l'utilisateur qui ajoute le message.
	 * @param content : contenu du commentaire.
	 * @param idMessage : id du message commenté.
	 */
	public static void addComment(String key, String content, String idMessage){
		
		try {
			DBCollection commentaire  = Database.getConnection("comments");
			DBCollection messages = Database.getConnection("messages");
			BasicDBObject dbo = new BasicDBObject();
			GregorianCalendar calendar = new GregorianCalendar();
			Date date_jour = calendar.getTime();
						
			int idUser = UserTools.getIdUserByKey(key);
			
			Random rand = new Random();
			
			dbo.put("id_commentaire", key+rand.nextInt(100));
			dbo.put("id_message", Integer.parseInt(idMessage));
			dbo.put("user_id", idUser);
			dbo.put("user_login", bd.UserTools.getLoginById(idUser));
			dbo.put("date", date_jour );
			dbo.put("content", content);
	
			commentaire.insert(dbo);
			
			BasicDBObject rechercheMessage = new BasicDBObject("_id", Integer.parseInt(idMessage));
			BasicDBObject maj = new BasicDBObject("$push", new BasicDBObject("comments", dbo));
			
			messages.update(rechercheMessage, maj);
			
			// Mise à jour du nombre de commentaires.
			DBObject dbmess =  messages.findOne(rechercheMessage);
			
			BasicDBObject majNbCom = new BasicDBObject("$inc", new BasicDBObject("nb_comments",1));
			messages.update(rechercheMessage, majNbCom);
			
			
						
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	
		
	}
	
	/**
	 * Supprime un commentaire de la base de données.
	 * @param key : clé de l'utilisateur qui supprime le commentaire.
	 * @param idComment : id du commentaire à supprimer.
	 * @param idMessage : id du message commenté.
	 */
	public static void deleteComment(String key, String idComment, String idMessage) {
		
		try {
			
			DBCollection comments = Database.getConnection("comments");
			
			BasicDBObject dbo = new BasicDBObject("id_commentaire", idComment);

			comments.remove(dbo);
			
			DBCollection messages = Database.getConnection("messages");
			
			BasicDBObject rechercheMessage = new BasicDBObject("_id", Integer.parseInt(idMessage));
			BasicDBObject maj = new BasicDBObject("comments", new BasicDBObject("id_commentaire", idComment));
			
			messages.update(rechercheMessage, new BasicDBObject("$pull", maj)); 
			
			// Mise à jour du nombre de commentaires.
			DBObject dbmess =  messages.findOne(rechercheMessage);
			
			BasicDBObject majNbCom = new BasicDBObject("$inc", new BasicDBObject("nb_comments", -1));
			messages.update(rechercheMessage, majNbCom);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Renvoie tout les commentaires d'un message.
	 * @param idMessage : id du message
	 * @return un JSON contenant tout les commentaires.
	 */
	public static JSONObject getAllCommentsForMessage(String idMessage){
		JSONObject reponse = new JSONObject();
		
		try {
				DBCollection messages = Database.getConnection("messages");
				
				BasicDBObject message = new BasicDBObject();
				message.put("_id", Integer.parseInt(idMessage));

				// Vérification de l'existance du message dans la base de données.
				DBCursor curseur = messages.find(message);
				
				if (!curseur.hasNext()) {
					return null;
				}
				
				JSONObject reponse_message = new JSONObject(JSON.serialize(curseur.next()));
				JSONArray commentaires = reponse_message.getJSONArray("comments");
				
				reponse.put("comments", commentaires);
			
				return reponse;
				
		}catch (JSONException | UnknownHostException e) {
			e.printStackTrace();
		}
		
		return reponse;
	}

	public static void main(String args[]) {
		//addComment("ni12ykee9sifuoi8vx6k", "commentaire ouloiu", "ni12ykee9sifuoi8vx6k57");
		//System.out.println(getAllCommentsForMessage("ni12ykee9sifuoi8vx6k91"));
		//deleteComment("jpkplkgn3bnb2v6pe8it", "ni12ykee9sifuoi8vx6k90", "ni12ykee9sifuoi8vx6k57");
		//deleteComment("jpkplkgn3bnb2v6pe8it", "ni12ykee9sifuoi8vx6k63", "ni12ykee9sifuoi8vx6k57");
		//deleteComment("jpkplkgn3bnb2v6pe8it", "jpkplkgn3bnb2v6pe8it89", "ni12ykee9sifuoi8vx6k57");
		//System.out.println(getAllCommentsForMessage("ni12ykee9sifuoi8vx6k91"));
	}
}
