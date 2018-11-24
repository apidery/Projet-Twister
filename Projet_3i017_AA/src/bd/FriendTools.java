package bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendTools {

	public FriendTools(){
		
	}
	
	
	public static void addFriend(String loginFriend, String key){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			// Récupérer l'id de l'ami que l'ont veut ajouter
			String querySelect = "SELECT * FROM user WHERE login = \""+loginFriend+"\";";
			Statement stFriend = c.createStatement();
			stFriend.executeQuery(querySelect);
			ResultSet curseur = stFriend.getResultSet();
			
			int friend = 0;
			
			if(curseur.next()){ // utilisateur exists
				friend = curseur.getInt(1);
			}
			
			int idUser = UserTools.getIdUserByKey(key);
			
			String query = "INSERT INTO friends (idUser, friend, ntimestamp) values (\""+idUser+"\",\""+friend+"\",CURRENT_TIMESTAMP);";
			Statement st = c.createStatement();
			int valide = st.executeUpdate(query);
			
			stFriend.close();
			st.close();
			c.close();
		
			
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
		}

	}
	
	public static void deleteFriend(String loginFriend, String key){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			// Récupérer l'id de l'ami que l'ont veut supprimer
			String querySelect = "SELECT * FROM user WHERE login = \""+loginFriend+"\";";
			Statement stFriend = c.createStatement();
			stFriend.executeQuery(querySelect);
			ResultSet curseur = stFriend.getResultSet();
			
			int friend = 0;
			
			if(curseur.next()){ // utilisateur exists
				friend = curseur.getInt(1);
			}
			
			int idUser = UserTools.getIdUserByKey(key);
			
			String query = "DELETE FROM friends WHERE idUser = \""+idUser+"\" and friend = \""+friend+"\";";
			Statement st = c.createStatement();
			int valide = st.executeUpdate(query);
			
			stFriend.close();
			st.close();
			c.close();
		
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
		}
	}
	
	public static List<Integer> listFriend(String key){
		
		List<Integer> listId = new ArrayList<Integer>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			int idUser = UserTools.getIdUserByKey(key);
		
			// Récupérer la liste d'amis.
			String query = "SELECT friend FROM friends WHERE idUser = \""+idUser+"\";";
			Statement st = c.createStatement();
			st.executeQuery(query);
			ResultSet curseur = st.getResultSet();
			
			while(curseur.next()){
				listId.add(curseur.getInt(1));
			}
			
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
		}
		
		return listId;
	}
	
	public static List<Integer> listFriendByLogin(String login){
		
		List<Integer> listId = new ArrayList<Integer>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			int idUser = UserTools.getIdUserByLogin(login);
		
			// Récupérer la liste d'amis.
			String query = "SELECT friend FROM friends WHERE idUser = \""+idUser+"\";";
			Statement st = c.createStatement();
			st.executeQuery(query);
			ResultSet curseur = st.getResultSet();
			
			while(curseur.next()){
				listId.add(curseur.getInt(1));
			}
			
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
		}
		
		return listId;
	}

	public static List<Integer> imTheirFriend(int idUser){
		
		List<Integer> listId = new ArrayList<Integer>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			// Récupérer la liste d'amis.
			String query = "SELECT idUser FROM friends WHERE friend = \""+idUser+"\";";
			Statement st = c.createStatement();
			st.executeQuery(query);
			ResultSet curseur = st.getResultSet();
			
			while(curseur.next()){
				listId.add(curseur.getInt(1));
			}
			
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return listId;
	}
	
	public static JSONObject usersExceptMe(String key) {
		
		
		JSONObject rep = new JSONObject();
		JSONArray tab = new JSONArray();
		JSONObject repfinal = new JSONObject();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			int iduser = bd.UserTools.getIdUserByKey(key);
			
			// Récupérer tout les utilisateur sauf l'utilisateur connecté.
			String querySelect = "SELECT id, login FROM user WHERE id != \""+iduser+"\" ORDER BY RAND() LIMIT 5;";
			Statement stFriend = c.createStatement();
			stFriend.executeQuery(querySelect);
			ResultSet recommand = stFriend.getResultSet();	
			
			while(recommand.next()){
				rep = new JSONObject();
				rep.put("idMembre",recommand.getInt(1));
				rep.put("loginMembre", recommand.getString(2));
				if(isYourFriend(recommand.getString(2), key))
					rep.put("ami", true);
				else
					rep.put("ami", false);
				
				tab.put(rep);	
			}
			
			repfinal.put("membres", tab);
			
			stFriend.close();
			c.close();
			
		} catch (SQLException  | InstantiationException | IllegalAccessException | ClassNotFoundException | JSONException e) {
			e.printStackTrace();
		}
		
		
		return repfinal;
	}
	
	/**
	 * Vérifie que l'utilisateur "loginFriend" est ami avec l'utilisateur connecté.
	 * @param loginFriend : login de l'ami
	 * @param key : clé de connexion de l'utilisateur connecté
	 * @return retourne true si l'utilisateur est ami avec 'loginFriend', false sinon.
 	 */
	public static boolean isYourFriend(String loginFriend, String key){
		
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			// Récupérer l'id de l'ami
			String querySelect = "SELECT * FROM user WHERE login = \""+loginFriend+"\";";
			Statement stFriend = c.createStatement();
			stFriend.executeQuery(querySelect);
			ResultSet curseur = stFriend.getResultSet();
			
			int friend = 0;
			
			if(curseur.next()){ // utilisateur exists
				friend = curseur.getInt(1);
			}
			
			int idUser = UserTools.getIdUserByKey(key);
			
			String query = "SELECT * FROM friends WHERE idUser = \""+idUser+"\" and friend = \""+friend+"\";";
			Statement st = c.createStatement();
			st.executeQuery(query);
			curseur = st.getResultSet();
			
			if(curseur.next()){
				retour = true;
			}else{
				retour = false;
			}
			
			stFriend.close();
			st.close();
			c.close();
		
			
		}catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException  e) {
				e.printStackTrace();
		}
		
		return retour;
	}
	
}
