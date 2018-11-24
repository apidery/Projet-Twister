package services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import bd.MessageTools;
import bd.UserTools;

public class User {

	public User(){
		
	}
	
	public static JSONObject createUser(String nom, String prenom, String user, String motDePasse, String mail, String datenaiss){
		
		JSONObject rep = new JSONObject();
		
		if(!bd.UserTools.userExists(user)){
			try{
				rep.put("status", "ok");
				rep.put("nom", nom);
				rep.put("prenom", prenom);
				bd.UserTools.addUser(nom,prenom,user,motDePasse,mail,datenaiss);
				rep.put("etat : ", "utilisateur "+user+" ajouté.");
			}catch(JSONException e){
				e.printStackTrace();	
			}
			return rep;
			
		}else{
			return servicesTools.servicesRefused.servicesRefused("L'utilisateur existe déjà.", 1001);
		}
	}
	
	public static JSONObject deleteUser(String user){
		
		JSONObject rep = new JSONObject();
		
		if(bd.UserTools.userExists(user)){
			try{
				rep.put("status", "ok");
				bd.UserTools.deleteUser(user);
				rep.put("etat", "utilisateur "+user+" supprimé.");
				
			}catch(JSONException e){
				e.printStackTrace();	
			}
			
			return rep;
			
		}else{
			return servicesTools.servicesRefused.servicesRefused("L'utilisateur n'existe pas.", 1001);
		}
	}
	
	public static JSONObject login(String user, String mdp){
				
		JSONObject ret = new JSONObject();
		
		if(user == null || mdp == null){
			ret = servicesTools.servicesRefused.servicesRefused("Il manque des parametres", 1001);	
		}
		else{
			if(bd.UserTools.userExists(user)){
				if(bd.UserTools.checkPassword(user, mdp)){
					if(!bd.UserTools.isConnected(user)) {
						String key = bd.UserTools.insertConnexion(user, 0);
						int id = bd.UserTools.getIdUserByKey(key);
						List<Integer> follows = bd.FriendTools.listFriend(key);
						int nbAbonne = bd.FriendTools.imTheirFriend(id).size();
						
						try{
							ret.put("status", "ok");
							ret.put("key", key);
							ret.put("id",id);
							ret.put("login", user);
							ret.put("nom", bd.UserTools.getNomUserById(id));
							ret.put("prenom", bd.UserTools.getPrenomUserById(id));
							ret.put("date_naiss", bd.UserTools.getDateNaissanceUserById(id));
							ret.put("follows", follows);
							ret.put("nbAbo", nbAbonne);
							ret.put("nbMessagesTotal", MessageTools.getNbMessage(id));
							ret.put("nbMessagesUser", MessageTools.getNbMessageUser(id));
							ret.put("color", UserTools.getColorProfilUser(id));
						}
						catch(JSONException e){
							e.printStackTrace();
						}
					}else {
						ret = servicesTools.servicesRefused.servicesRefused("L'utilisateur est déjà connecté.", 1001);
					}
				}
				else{
					ret = servicesTools.servicesRefused.servicesRefused("Le mot de passe est incorrect.", 1001);
				}
			}
			else{
				ret = servicesTools.servicesRefused.servicesRefused("L'utilisateur n'existe pas.", 1001);
			}
		}
		
		return ret;
	}

	public static JSONObject logout(String key){
		
		JSONObject ret = new JSONObject();
		
		if(key == null){
			ret = servicesTools.servicesRefused.servicesRefused("Il manque des parametres", 1001);
		}
		else{
			if(bd.UserTools.keyExists(key)){
				if(bd.UserTools.deleteConnexion(key)) {
					try{
						System.out.println("Déconnecté!");
						ret.put("status", "ok");
						ret.put("etat", "deconnecté");
					}
					catch(JSONException e){
						e.printStackTrace();
					}	
				}else {
					ret = servicesTools.servicesRefused.servicesRefused("L'utilisateur n'est pas déconnecté", 1001);
				}	
			}else{
				ret = servicesTools.servicesRefused.servicesRefused("L'utilisateur n'est pas connecté", 1001);	
			}	
		}
		
		return ret;
	}
	
	public static JSONObject infoUser(String login){
		JSONObject ret = new JSONObject();
		
		if(login == null){
			ret = servicesTools.servicesRefused.servicesRefused("Il manque des parametres", 1001);	
		}
		else{
			if(bd.UserTools.userExists(login)){						
				int id = bd.UserTools.getIdUserByLogin(login);
				List<Integer> follows = bd.FriendTools.listFriendByLogin(login);
				int nbAbonne = bd.FriendTools.imTheirFriend(id).size();
				
				try{
					ret.put("status", "ok");
					ret.put("id",id);
					ret.put("login", login);
					ret.put("nom", bd.UserTools.getNomUserById(id));
					ret.put("prenom", bd.UserTools.getPrenomUserById(id));
					ret.put("date_naiss", bd.UserTools.getDateNaissanceUserById(id));
					ret.put("follows", follows);
					ret.put("nbAbo", nbAbonne);
				}
				catch(JSONException e){
					e.printStackTrace();
				}					
			}
			else{
				ret = servicesTools.servicesRefused.servicesRefused("L'utilisateur n'existe pas.", 1001);
			}
		}
		
		return ret;
	}
	
	public static JSONObject someoneIsConnected(){
		JSONObject rep = new JSONObject();
		
		try{
			rep.put("information", bd.UserTools.someoneIsConnected());
		}catch(JSONException e) {
			e.printStackTrace();
		}
		
		return rep;
	}
	/**
	 * Insert une couleur pour le profil de l'utilisateur.
	 * @param color : couleur à ajouter.
	 * @return JSON informant sur la bonne modification de la couleur.
	 */
	public static JSONObject changeColor(String key, String color){
		JSONObject reponse = new JSONObject();
		
		if(bd.UserTools.keyExists(key)){
			try{
				bd.UserTools.changeColor(key, color);
				reponse.put("status", "ok");
				reponse.put("color", color);
			}catch(JSONException e){
				e.printStackTrace();	
			}
			return reponse;
			
		}else{
			return servicesTools.servicesRefused.servicesRefused("L'utilisateur n'est pas connecté.", 1001);
		}
	}
	
	public static void main(String args[]) {
		//System.out.println(createUser("Tarrieu", "Axel", "atarrieu", "francois972", "axel@gmail.com", "1997/04/02"));
		//System.out.println(deleteUser("atarrieu"));
		//System.out.println(login("dybala","argentine"));
		
		System.out.println(changeColor("17ymtx7sd8zyvi3nbdbm", "red"));
	}
		
}
