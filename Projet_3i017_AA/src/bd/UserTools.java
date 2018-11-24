package bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.mysql.jdbc.PreparedStatement;

public class UserTools {

	public UserTools(){
		
	}
	
	/** Retourne un boolean indiquant l'existance de l'utilisateur "login" dans la base de données.
	 * @param login : Le login de l'utilisateur dont on veut verifier l'existence
	 * @return Un boolean a true si le login est dans la base de données, false sinon.
	 */
	
	public static boolean userExists(String login) {
			boolean retour = false;
			
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection c = Database.getMySQLConnection();
				
				String query = "SELECT * FROM user WHERE login = ?";
				
				java.sql.PreparedStatement st = c.prepareStatement(query);
				st.setString(1, login);
				st.execute();
				
				ResultSet curseur = st.getResultSet();
				
				if(curseur.next()){ // utilisateur exists
					retour = true;
				}
				else{
					retour = false;
				}
				
				curseur.close();
				st.close();
				c.close();
			
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return retour;
		
	}
	
	/** Retoune l'id correspondant au login.
	 * @param login : le login dont on veut l'id.
	 * @return un entier correspondant à l'id.
	 */
	
	public static int getIdUserByLogin(String login) {
		int idUser = -1;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT id FROM user WHERE login = ?";
			
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setString(1, login);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){
				idUser = curseur.getInt(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return idUser;
	}
	
	

	/** Retoune l'id correspondant à la clé de connexion.
	 * @param key : la clé de l'utilisateur connecté, dont on veut l'id.
	 * @return un entier correspondant à l'id.
	 */
	
	public static int getIdUserByKey(String key) {
		
		int idUser = -1;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT idUser FROM new_session WHERE cle = ?";
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setString(1, key);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){ // utilisateur exists
				idUser = curseur.getInt(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return idUser;
	}
	
	/**
	 * Renvoi le login d'un utilisteur grace à son id.
	 * @param id : id de l'utilisateur.
	 * @return le login de l'utilisateur.
	 */
	public static String getLoginById(int id) {
		String loginUser = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT login FROM user WHERE id = ?";
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setInt(1, id);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){ // utilisateur exists
				loginUser = curseur.getString(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return loginUser;
	}
	
	/**
	 * Renvoie le nom d'un utilisteur grace à son id.
	 * @param id : id de l'utilisateur.
	 * @return le nom de l'utilisateur.
	 */
	public static String getNomUserById(int id) {
		String nom = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT nom FROM user WHERE id = ?";
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setInt(1, id);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){ // utilisateur exists
				nom = curseur.getString(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return nom;
	}
	
	
	/**
	 * Renvoie le prenom d'un utilisteur grace à son id.
	 * @param id : id de l'utilisateur.
	 * @return le prenom de l'utilisateur.
	 */
	public static String getPrenomUserById(int id) {
		String prenom = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT prenom FROM user WHERE id = ?";
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setInt(1, id);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){ // utilisateur exists
				prenom = curseur.getString(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return prenom;
	}
	
	/**
	 * Renvoie la date de naissance d'un utilisteur grace à son id.
	 * @param id : id de l'utilisateur.
	 * @return la date de naissance de l'utilisateur.
	 */
	public static String getDateNaissanceUserById(int id) {
		String datenaiss = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT datenaissance FROM user WHERE id = ?";
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setInt(1, id);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){ // utilisateur exists
				datenaiss = curseur.getString(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return datenaiss;
	}
	
	/**
	 * Ajoute un utilisateur et ses informations à la base de données.
	 * @param nom : nom de l'utilsateur à ajouter
	 * @param prenom : prenom de l'utilsateur à ajouter
	 * @param login : login de l'utilsateur à ajouter
	 * @param mdp : mot de passe de l'utilsateur à ajouter
	 * @param mail : adresse mail de l'utilsateur à ajouter
	 */
	public static void addUser(String nom, String prenom, String login, String mdp, String mail, String datenaiss){
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String query = "INSERT INTO user (login, mail, nom, prenom, pwd, datenaissance) values (?,?,?,?,PASSWORD(?),CONVERT(?, DATETIME))";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, login);
			st.setString(2, mail);
			st.setString(3, nom);
			st.setString(4, prenom);
			st.setString(5, mdp);
			
			String tmp[] = datenaiss.split("-");
			
			st.setString(6, datenaiss);
			
			st.executeUpdate();
			
			st.close();
			c.close();

		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Supprime l'utilisateur à qui appatient le login.
	 * @param login : login de l'utilisateur à supprimer
	 */
	public static void deleteUser(String login){
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection c = Database.getMySQLConnection();
			String query = "DELETE FROM user WHERE login = ?;";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, login);
			
			st.executeUpdate();
			
			st.close();
			c.close();
			
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Vérifie que le mot de passe correspond à celui présent pour le login dans la base de données.
	 * @param login : login de l'utilisateur
	 * @param mdp : mot de passe de l'utilisateur
	 * @return true si le couple login/mot de passe est dans présent dans la base de données, false sinon.
	 */
	public static boolean checkPassword (String login, String mdp){
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection c = Database.getMySQLConnection();
			String query = "SELECT * FROM user WHERE login = ? AND pwd = PASSWORD(?)";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, login);
			st.setString(2, mdp);
			
			st.execute();
			
			ResultSet curseur = st.getResultSet();
			
			if(curseur.next()){
				retour = true;
			}
			else{
				retour = false;
			}
			
			curseur.close();
			st.close();
			c.close();
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		
		return retour;
	}
	
	/**
	 * Vérifie sur l'utilisateur est connecté.
	 * @param login : login de l'utilisateur
	 * @return true si l'utilisateur est connecté, false sinon.
	 */
	public static boolean isConnected(String user) {
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
						
			int idUser = getIdUserByLogin(user);
			
			if(idUser == -1) {
				return false;
			}
			
			String query = "SELECT * FROM new_session WHERE  idUser = ?";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setInt(1, idUser);
			st.execute();
			
			ResultSet curseur = st.getResultSet();
			
			if(curseur.next()){ 
				retour = true;
			}
			else{
				retour = false;
			}
			
			curseur.close();
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return retour;
		
	}
	
	public static boolean someoneIsConnected() {
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			String query = "SELECT * FROM new_session";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.execute();
			
			ResultSet curseur = st.getResultSet();
			
			if(curseur.next()){ 
				retour = true;
			}
			else{
				retour = false;
			}
			
			curseur.close();
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return retour;
	}
	
	/**
	 * Insert les informations correspondant à une connexion dans la base de données.
	 * @param login : login de l'utilisateur qui se connecte.
	 * @param root : entier indiquant si l'utilisateur se connecte en tant qu'admin (1) ou pas (0).
	 * @return la clé de connextion de l'utilisateur.
	 */
	public static String insertConnexion (String user, int root){
		String key = "";
		
		if(isConnected(user)) {
			return "error";
		}else {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection c = Database.getMySQLConnection();
				
				// Création d'une clé.
				Random rand = new Random();
				String alphabet = "abcdefghijklmnopqrstuvwxyz12356789";
				int longueur = alphabet.length();
				
				for(int i = 0; i < 20; i++) {
				  int k = rand.nextInt(longueur);
				  key+= alphabet.charAt(k);
				}
				
				int idUser = getIdUserByLogin(user);
				
				if(idUser == -1) {
					return "error";
				}
				
				String query = "INSERT INTO new_session (idUser, cle, dateConnexion, isRoot) values (?,?,CURRENT_TIMESTAMP ,?);";
				java.sql.PreparedStatement st = c.prepareStatement(query);
				st.setInt(1, idUser);
				st.setString(2, key);
				st.setInt(3, root);
				
				st.executeUpdate();
				
				st.close();
				c.close();
				
			}catch (InstantiationException |IllegalAccessException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
			return key;
		}
	}
	
	/**
	 * Supprime les informations correspondant à la clé de connexion.
	 * @param key : clé de la connexion à supprimer.
	 * @return true si la suppression a été réalisé, false sinon.
	 */
	
	public static boolean deleteConnexion(String key) {
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			
			String query = "DELETE FROM new_session WHERE cle = ?";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, key);
			int valide = st.executeUpdate();
			
			if(valide == 1){ 
				retour = true;
			}
			else{
				retour = false;
			}
		
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return retour;
	}
	
	/**
	 * Vérifie si l'utilisateur est admin.
	 * @param login : login de l'utilisateur
	 * @return true si l'utilisateur est admin, false sinon.
	 */
	public static boolean isRoot(String login){
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection c = Database.getMySQLConnection();
			
			String query = "SELECT isRoot FROM new_session, user WHERE user.login = ? AND new_session.idUser = user.id;";				
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, login);
			st.execute();
			
			ResultSet curseur = st.getResultSet();
			
			if(curseur.next()){ 					
				if(curseur.getInt(1) == 1){
					retour = true;
				}
			}
			else{
				retour = false;
			}
			
			curseur.close();
			st.close();
			c.close();
			
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		
		return retour;
	}
	
	/**
	 * Vérifie qu'une clé de connexion est présente dans la base de données.
	 * @param key : clé à vérifier.
	 * @return true si le clé est présente dans la base de données, false sinon.
	 */
	public static boolean keyExists(String key){
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String query = "SELECT * FROM new_session WHERE cle = ?";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, key);
			st.execute();
			
			ResultSet curseur = st.getResultSet();
			
			if(curseur.next()){ 
				retour = true;
			}
			else{
				retour = false;
			}
			
			curseur.close();
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return retour;
	}
	
	/**
	 * Vérifie si la connection est expirée.
	 * @param key : clé de la connexion à vérifier.
	 * @return true si la connexion a expirée, false sinon.
	 */
	public static boolean sessionNotExpired(String key){
		
		boolean retour = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String query = "SELECT * FROM new_session WHERE cle = ? AND dateConnexion BETWEEN timestamp(DATE_SUB(NOW(), INTERVAL 30 MINUTE)) AND timestamp(NOW());";
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1,key);
			st.execute();
			ResultSet curseur = st.getResultSet();
		 
			if(curseur.next()){
				rechargedSession(key);
				retour = true;
			}else{
				deleteConnexion(key);
			}
			
			curseur.close();
			st.close();
			c.close();		
			
		}catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return retour;
		
	}

	/**
	 * Met à jour une session.
	 * @param key : clé de la session à mettre à jour.
	 */
	public static void rechargedSession(String key){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		
			Connection c = Database.getMySQLConnection();
			String query = "UPDATE new_session SET dateConnexion = NOW() WHERE cle = ?";				
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, key);
			st.executeUpdate();
			
			st.close();
			c.close();
			
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	
	public static void changeColor(String key, String color) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			Connection c = Database.getMySQLConnection();
			
			int iduser = UserTools.getIdUserByKey(key);
			
			String query = "UPDATE user SET color = ? WHERE id = ?";				
			java.sql.PreparedStatement st = c.prepareStatement(query);
			st.setString(1, color);
			st.setInt(2, iduser);
			st.executeUpdate();
			
			st.close();
			c.close();
			
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public static String getColorProfilUser(int id) {
		String color = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection c = Database.getMySQLConnection();
			String querySelectUser = "SELECT color FROM user WHERE id = ?";
			java.sql.PreparedStatement stUser = c.prepareStatement(querySelectUser);
			stUser.setInt(1, id);
			stUser.execute();
			
			ResultSet curseur = stUser.getResultSet();
			
			if(curseur.next()){ // utilisateur exists
				color = curseur.getString(1);
			}
			
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 		

		return color;
	}
	
	public static void main(String args[]){
		
		//addUser("Pidery", "Fabrice", "fpidery", "francois972", "fab@live.fr", "1998-02-18"); // Test ajout. 
		//System.out.println(userExists("sosa")); // Test user présent dans BD.
		
		//addToBDUser("User", "user", "delete", "test1"); // Test suppression.
		//deleteToBDUser("delete");
		
		//System.out.println(checkPassword("sosa", "test1")); //Test mdp correct.
		//System.out.println(checkPassword("sosa", "test2")); //Test mdp incorrect.
		
		//System.out.println(insererConnexion("sosa", 1)); //Test inserer connexion. 
		//System.out.println(insererConnexion("sosa", 1)); // Affiche error car connexion deja inserrée.
		
		//System.out.println(someoneIsConnected());
		
		//changeColor("17ymtx7sd8zyvi3nbdbm", "blue");
		
		//System.out.println(getColorProfilUser(22));
	
	}
}
