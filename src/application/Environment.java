package application;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Environment class
 * @author Kacper Wnuk 4I1 Informatyka
 */


public class Environment {
	String name;
	ObservableList<String> users;
	Map<String, String> passwords;
	
	/**
	 * Environment constructor, sets data using jsonfile
	 * @param name environment name
	 */
	
	public Environment(String name) {
		this.name = name;
		users = FXCollections.observableArrayList(); 
		users = getUserData(name);
		passwords = new HashMap<>();
		setPasswords();		
	}
	
	/**
	 * @return name of environment 
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return list of users in environment
	 */
	public ObservableList<String> getUsers(){
		return users;
	}
	
	/**
	 * Uploads user passwords from json file  
	 * @exception IOException ParseException
	 */
	private void setPasswords() {
		
		JSONParser parser = new JSONParser();  
		try {
			Object object = parser.parse(new FileReader("database.json"));
			JSONObject jsonObject = (JSONObject) object;

			users.forEach(el -> {
				passwords.put(el, (String)jsonObject.get(el) );
				//System.out.printf("%s %s", name, el);
			});
		
		}  catch (IOException e1) {
			
			e1.printStackTrace();
		}
			catch (ParseException e) {
			
			e.printStackTrace();
		}
	
		
	}
	
	/**
	 * Uploads user data from json file
	 * @param env environment from which users are needed (String)
	 * @exception IOException ParseException
	 */
	private ObservableList<String> getUserData(String env){
		
		JSONParser parser = new JSONParser();  
		JSONArray arr = new JSONArray();
		try {
			Object object = parser.parse(new FileReader("database.json"));
			 
			JSONObject jsonObject = (JSONObject) object;
			arr = (JSONArray) jsonObject.get(env);
		
		}  catch (IOException e1) {
			
			e1.printStackTrace();
		}
			catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		ObservableList<String> values = FXCollections.observableArrayList();
		for(int i = 0; i < arr.size();  ++i)
		{
			values.add((String) arr.get(i));
		}
		return values;
	}
	
	/**
	 * @param user user whose password is needed (String)
	 * @return user password
	 */
	public String getUserPass(String user) {
		return passwords.get(user);
	}
	
	/**
	 * Updating environment class when new user is added
	 * @param user user (String)
	 * @param pass password (String)  
	 */
	public void addUser(String user, String pass){
		users.add(user);
		passwords.put(user, pass);		
	}
	
	
	/**
	 * Used for example during parsing environment types to file 
	 */
	@Override
	public String toString() {
		return name;
	}
	
}
