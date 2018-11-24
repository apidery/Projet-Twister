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

import javax.jws.soap.SOAPBinding.Use;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MessageTools {
	
	public MessageTools(){
		
	}

	/**
	 * Création d'un message et ajout dans la base de données.
	 * @param key : clé de l'utilisateur qui a posté le message.
	 * @param content : contenu du message.
	 */
	public static int addMessage(String key, String content){
		
		int id_new_message = -1;
		
		try {
			DBCollection message  = Database.getConnection("messages");
			DBCollection counter = Database.getConnection("counter");
			
			BasicDBObject dbo = new BasicDBObject();
			GregorianCalendar calendar = new GregorianCalendar();
			Date date_jour = calendar.getTime();
		
			int idUser = UserTools.getIdUserByKey(key);
				
			Random rand = new Random();
			
			// Si aucun message n'est enregistré.
			if(counter.find().count() == 0) {
				createCounter();
			}
			
			id_new_message = getNewIdMessage();
			dbo.put("_id", id_new_message);
			dbo.put("user_id", idUser);
			dbo.put("date", date_jour );
			dbo.put("content", content);
			dbo.put("comments", new ArrayList<BasicDBObject>());
			dbo.put("nb_comments", 0);
			dbo.put("likes", new ArrayList<BasicDBObject>());
			dbo.put("nb_likes", 0);
			
			message.insert(dbo);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		
		return id_new_message;
	}
	
	/**
	 * Supprime un message de la base de données.
	 * @param key : clé de l'utilisateur qui supprime le message.
	 * @param idMessage : id du message à supprimer.
	 */
	public static void deleteMessage(String key, String idMessage) {
		try {
			
			int idUser = UserTools.getIdUserByKey(key);
			
			DBCollection message  = Database.getConnection("messages");
			BasicDBObject dbo = new BasicDBObject();
			
			dbo.put("_id", Integer.parseInt(idMessage));
			dbo.put("user_id", idUser);
			
			message.remove(dbo);
			
		} catch (UnknownHostException e) {	
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * Renvoie tout les message d'un utilisateur.
	 * @param key : clé de l'utilisateur qui veut voir les messages.
	 * @param id_user : id de l'utilisateur dont on veut les messages.
	 * @return une liste de messages.
	 */
	public static List<DBObject> getMessagesUser(String key, int id_user){
		
		List<DBObject> ret = new ArrayList();
		
		DBCollection message;
		
		try {			
			message = Database.getConnection("messages");
			
			BasicDBObject query = new BasicDBObject();
			query.put("user_id", id_user);
			
			DBCursor c = message.find(query).sort(new BasicDBObject("_id",-1));
		
			while(c.hasNext()){
				ret.add(c.next());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param key
	 * @param recherche
	 * @param id_max
	 * @param id_min
	 * @param limit
	 * @return
	 */
	public static List<DBObject> getMessagesUsers(String key, String recherche, String id_max, String id_min, String limit){
  		
		List<DBObject> reponse = new ArrayList();
		
		try {
			// On se connecte a la BDD puis on recupere les messages
			DBCollection messages = Database.getConnection("messages");
			  		
			int idUser = UserTools.getIdUserByKey(key);
			List<Integer> id_amis = FriendTools.listFriend(key);
			
			id_amis.add(idUser);
			
			// Creation de la requete
			BasicDBObject requete = new BasicDBObject();
			ArrayList<BasicDBObject> and = new ArrayList<BasicDBObject>();
			
			if (! id_min.equals("-1")) {
				and.add(new BasicDBObject("_id", new BasicDBObject("$gte", Integer.parseInt(id_min))));
			}
			if (! id_max.equals("-1")) {
				and.add(new BasicDBObject("_id", new BasicDBObject("$lte", Integer.parseInt(id_max))));
			}
			if (and.size() != 0) {
				requete.put("$and", and);
			}
			
			requete.put("user_id", new BasicDBObject("$in", id_amis));
			
			DBCursor curseur = messages.find(requete).sort(new BasicDBObject("_id", -1)).limit(Integer.parseInt(limit));
			
			while (curseur.hasNext()) {
				reponse.add(curseur.next());
			}
			
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		
		return reponse;
		
	}
	
	
	/**
	 * Vérifie si le message est à cet utilisateur.
	 * @param key : clé de l'utilisateur.
	 * @param idMessage : id du message.
	 * @return true si le message a bien été posté par l'utilisateur, false sinon.
	 */
	public static boolean isYourMessage(String key, String idMessage){
		
		boolean retour = false;
		DBCollection message;
		
		try {
			
			int idUser = UserTools.getIdUserByKey(key);
			
			message = Database.getConnection("messages");
			BasicDBObject query = new BasicDBObject();
			query.put("_id", Integer.parseInt(idMessage));
			
			DBCursor c = message.find(query);
			
			DBObject obj = null;
			
			
			if(c.hasNext()){
				obj = c.next();
				if(Integer.parseInt(obj.get("user_id").toString()) == idUser){
					retour = true;
				}	
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		
		return retour;
	}
	
	/* Likes */
	
	/**
	 * Ajoute un "like" à un message.
	 * @param key : clé de l'utilisateur qui a aimé le message.
	 * @param idMessage : id du message aimé.
	 */
	public static void addLike(String key, String idMessage){

		try {
			
			DBObject likeExist = alreadyLike(key, Integer.parseInt(idMessage));
				
				if(likeExist != null) {
					dislike(key, idMessage, likeExist.get("id_like"));
				}else {
					DBCollection likes  = Database.getConnection("likes");
					DBCollection messages = Database.getConnection("messages");
					BasicDBObject dbo = new BasicDBObject();
					GregorianCalendar calendar = new GregorianCalendar();
					Date date_jour = calendar.getTime();
					
					int idUser = UserTools.getIdUserByKey(key);
					
					Random rand = new Random();
					
					String idLike = key+rand.nextInt(100);
					
					dbo.put("id_like", key+rand.nextInt(100));
					dbo.put("id_message", Integer.parseInt(idMessage));
					dbo.put("user_id", idUser);
					dbo.put("user_login", bd.UserTools.getLoginById(idUser));
					dbo.put("date", date_jour );
			
					likes.insert(dbo);
		
					
					BasicDBObject rechercheMessage = new BasicDBObject("_id", Integer.parseInt(idMessage));
					BasicDBObject maj = new BasicDBObject("$push", new BasicDBObject("likes", dbo));
					
					messages.update(rechercheMessage, maj);
					
					// Mise à jour du nombre de likes.					
					BasicDBObject majNbLikes = new BasicDBObject("$inc", new BasicDBObject("nb_likes", 1));
					messages.update(rechercheMessage, majNbLikes);
				}
						
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Supprime le like du message. 
	 * @param key : clé de l'utilisateur qui n'aime plus le message.
	 * @param idMessage : id du message.
	 * @param idLike : id du like à supprimer.
	 */
	
	public static void dislike(String key, String idMessage, Object idLike) {
		try {
			
			DBCollection likes = Database.getConnection("likes");
			
			BasicDBObject dbo = new BasicDBObject("id_message", Integer.parseInt(idMessage));

			likes.remove(dbo);
			
			DBCollection messages = Database.getConnection("messages");
			
			BasicDBObject rechercheMessage = new BasicDBObject("_id", Integer.parseInt(idMessage));
			BasicDBObject maj = new BasicDBObject("likes", new BasicDBObject("id_like", idLike));
			
			messages.update(rechercheMessage, new BasicDBObject("$pull", maj)); 
			
			// Mise à jour du nombre de likes.
			BasicDBObject majNbLikes = new BasicDBObject("$inc", new BasicDBObject("nb_likes", -1));
			messages.update(rechercheMessage, majNbLikes);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Vérifie si l'utilisateur à déjà aimé le message.
	 * @param key : clé de l'utilisateur.
	 * @param idMessage : id du message.
	 * @return le "like" si le message à déjà été aimé par cet utilisateur.
	 */
	public static DBObject alreadyLike(String key, int idMessage) {
		
		DBObject dbmess = null;
		
		try {
			DBCollection likes  = Database.getConnection("likes");
			DBCollection messages = Database.getConnection("messages");
			BasicDBObject dbo = new BasicDBObject();
			
			int idUser = UserTools.getIdUserByKey(key);
			
			// Pour savoir si il existe un "like" de l'utilisateur pour un message précis.
			 
			BasicDBObject rechercheMessage = new BasicDBObject("id_message", idMessage);
			BasicDBObject rechercheUser = new BasicDBObject("user_id", idUser);
			
			BasicDBObject and[] = {rechercheMessage,rechercheUser};
			
			BasicDBObject query = new BasicDBObject("$and",and);
			
			dbmess = likes.findOne(query); 
			
		}catch ( UnknownHostException e) {
			e.printStackTrace();
		}		
		
		return dbmess;
	}
	
	/**
	 * Création du compteur pour l'auto increment des id des messages.
	 */
	public static void createCounter() {
		DBCollection counter;
		
		try {
			counter = Database.getConnection("counter");
			
			BasicDBObject doc = new BasicDBObject();
			
			doc.put("_id", "counter");
			doc.put("nb_messages", 0);
			
			counter.insert(doc);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Retourne l'id disponiblie pour le nouveau message crée, et incremente le compteur pour le message suivant.
	 * @return un entier reprensentant un identifiant unique.
	 */
	public static int getNewIdMessage() {
		DBCollection counter;
		int id = -1;
		
		try {
			counter = Database.getConnection("counter");
			
			 DBObject doc = counter.findAndModify(
		             new BasicDBObject("_id", "counter"), null, null, false,
		             new BasicDBObject("$inc", new BasicDBObject("nb_messages", 1)),
		             true, true);
			 
			 id = (Integer) doc.get("nb_messages");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public static int getNbMessageUser(int iduser) {
		int nbMessage = 0;
		
		try {			
			DBCollection message = Database.getConnection("messages");
			
			BasicDBObject query = new BasicDBObject();
			query.put("user_id", iduser);
			
			nbMessage = (int) message.count(query);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return nbMessage;
	}
	
	public static int getNbMessage(int iduser){
		
		int nbMessage = 0;
		
		try {			
			DBCollection message = Database.getConnection("messages");
			
			List<Integer> list_id = FriendTools.listFriendByLogin(UserTools.getLoginById(iduser));
			list_id.add(iduser);
			
			BasicDBObject query = new BasicDBObject();
			BasicDBObject list = new BasicDBObject();
			
			list.put("$in", list_id);
			query.put("user_id", list);
			
			nbMessage = (int) message.find(query).count();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return nbMessage;
		
	}
	
	
	public static void main(String args[]) {
		//addLike("eth1t99zh7va6p9w28ou", "qx7d57w1l3zfkmsicfn762");
		
		//System.out.println(alreadyLike("eth1t99zh7va6p9w28ou", "qx7d57w1l3zfkmsicfn762").get("id_like"));
		
		//createCounter();
		//System.out.println(getNewIdMessage());
		
		//addMessage("ua2zokw11cw7cu8rinyo", "test");
		
		//System.out.println(getMessagesUser("sx1czrw9ye8g2yf2r956", 23));
		
		//System.out.println(getNbMessage("jhm2ptp8uryr8ttybiec"));
		
	}
	
}
