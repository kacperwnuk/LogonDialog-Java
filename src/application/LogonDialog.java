package application;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * LogonDialog class based on dialog class
 * @author Kacper Wnuk 4I1 Informatyka
 */
public class LogonDialog {
	private String title;
	private String welcomeMassage;
	
	private Dialog<ButtonType> dialog = new Dialog<ButtonType>();
	private GridPane grid = new GridPane();
	private ChoiceBox<Environment> environment = new ChoiceBox<Environment>();
	private ComboBox<String> user = new ComboBox<String>();
	private PasswordField pass = new PasswordField();
	
	private ButtonType loginButton = new ButtonType("Logon", ButtonData.OK_DONE);
	private ButtonType cancelButton = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
	private Node logButton;
	
	private static int boxWidth = 200;
	
	/**
	 * LogonDialog constructor
	 * @param _title title of dialog window (String)
	 * @param _welcomeMessage welcome message (String)
	 */
	LogonDialog(String _title, String _welcomeMassage) {
		title = _title;
		welcomeMassage = _welcomeMassage;
		initialize();
	}
	
	/**
	 * Sets button to enable and disable mode
	 * @param value (Boolean) true - disable, false - enable
	 */
	private void setLogButton(Boolean value) {
		logButton = dialog.getDialogPane().lookupButton(loginButton);
		logButton.setDisable(value);
	}
	
	/**
	 * Setting dialog settings
	 */
	private void setDialog() {
		dialog.setTitle(title);
		dialog.setHeaderText(welcomeMassage);
		dialog.getDialogPane().getButtonTypes().addAll(loginButton, cancelButton);
	}
	
	/**
	 * Setting grid settings
	 */
	private void setGrid() {
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(20, 50, 20, 20));
		grid.add(new Label("Œrodowisko: "), 0, 0);
		grid.add(environment, 1, 0);
		grid.add(new Label("U¿ytkownicy: "), 0, 1);
		grid.add(user, 1, 1);
		grid.add(new Label("Has³o: "), 0, 2);
		grid.add(pass, 1, 2);
	}
	
	/**
	 * Uploading environment types from json file
	 * @param key 'Œrodowisko' label (String)
	 * @return ObservableList<Environment> list of all enivronments
	 */
	private ObservableList<Environment> getEnvs(String key){
		
		JSONParser parser = new JSONParser();  
		JSONArray arr = new JSONArray();
		try {
			Object object = parser.parse(new FileReader("database.json"));
			 
			JSONObject jsonObject = (JSONObject) object;
			arr = (JSONArray) jsonObject.get(key);
		
		}  catch (IOException e1) {
			
			e1.printStackTrace();
		}
			catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		ObservableList<Environment> values = FXCollections.observableArrayList();
		for(int i = 0; i < arr.size();  ++i)
		{
			values.add(new Environment((String) arr.get(i)));
		}
		return values;
	}
	
	/**
	 * Setting 'Œrodowisko' label settings
	 */
	private void setEnvironment() {
		
		environment.setMinWidth(boxWidth);
		environment.setPrefWidth(boxWidth);
		ObservableList<Environment> values = getEnvs("Srodowisko");
		
		environment.setConverter(new EnvConverter());
		environment.setItems(values);
		
		environment.valueProperty().addListener((observable, oldVal, newVal) -> env_Changed(newVal));
		
	}
	
	/**
	 * Checking if all labels have some data,
	 * if it`s true then set Logon button enable
	 * @localvariables envNotSet - 'environment not set' (boolean), usSet - 'userSet' (boolean), pNotSet - 'password not set' (boolean)  
	 * @localvariables isSet - 'all needed data set' (boolean)
	 */
	private void checkIfAllDataSet() {
		boolean envNotSet = environment.getSelectionModel().isEmpty();	
		boolean usSet = user.getValue() != null && !user.getValue().equals("");
		boolean pNotSet = pass.getText().equals("");
		boolean isSet = !envNotSet && usSet && !pNotSet;  
		
		if(isSet) {
			setLogButton(false);
		}
		else
		{
			setLogButton(true);
		}
	}
	
	/**
	 * Reacts to changes in 'Œrodowisko' label
	 * @param newVal new environment value (Environment)
	 * @localvariable values - list of users from new environment
	 */
	private void env_Changed(Environment newVal) {
		
		ObservableList<String> values = newVal.getUsers();
		user.setItems(values);
		
		checkIfAllDataSet();
	}
	
	/**
	 * Reacts to changes in password
	 * @param newVal - new password (String)
	 */
	private void pass_Changed(String newVal) {
		checkIfAllDataSet();		
		
	}
	
	/**
	 * Setting 'U¿ytkownik' label settings
	 */
	private void user_Changed(String newVal) {
		checkIfAllDataSet();
		
	}
			
	private void setUser() {
		user.setMinWidth(boxWidth);
		user.setPrefWidth(boxWidth);
		user.setEditable(true);
		user.valueProperty().addListener((observable ,oldVal, newVal)->user_Changed(newVal));
	}
	
	/**
	 * Setting 'Has³o' label settings
	 */
	
	private void setPass() {
		pass.setMinWidth(boxWidth);
		pass.setPrefWidth(boxWidth);
		pass.textProperty().addListener((observable ,oldVal, newVal)->pass_Changed(newVal));
	}
	
	/**
	 * Initializing dialog window
	 */
	
	private void initialize() {
	
		setDialog();
		setEnvironment();
		setUser();
		setPass();
		setLogButton(true);
		setGrid();
		dialog.getDialogPane().setContent(grid);
		
		Image image = new Image(ClassLoader.getSystemResourceAsStream("application/man.png"));
		ImageView imageView = new ImageView(image);
		dialog.setGraphic(imageView);
		
	}

	/**
	 * Checks if there is a user with a given password
	 * if password is incorrect then can`t log in
	 * if there is a new user then add him to database and log in
	 * @param env - environment (Environment), usr - user (String), passwd - password (String)
	 * @return true if everything correct or false if something went wrong 
	 */
	
	private boolean isPassCorrect(Environment env, String usr, String passwd) {
		
		ObservableList<String> users = env.getUsers();
		
		if(users.contains(usr) && passwd.equals(env.getUserPass(usr))) {
			return true;
		}
		else if(users.contains(usr) && !passwd.equals(env.getUserPass(usr))){
			return false;
		}
		else if(!users.contains(usr)) {
			return false;
			
		}
		
		return false;
	}
	
	/**
	 * Converts result from Optional<ButtonType> to Optional<Pair<Environment, String>>
	 * @param buttonType (Optional<ButtonType>)
	 * @return Pair<Environment, String>(environment.getValue(), user.getValue()) returns correct environment and user who logged in 
	 */
	
	private Optional<Pair<Environment, String>> resultConverter(Optional<ButtonType> buttonType) {
		
		if(buttonType.get() == loginButton) {
			if(isPassCorrect(environment.getValue(), user.getValue(), pass.getText())) {
			
				return Optional.of(new Pair<Environment, String>(environment.getValue(), user.getValue()));
			}
		}
		
		
		return Optional.empty();
	}
	
	/**
	 * Waiting for user response
	 * @return converted value from dialog.showAndWait()
	 */
	
	public Optional<Pair<Environment, String>> showAndWait() {
		return resultConverter(dialog.showAndWait());
	}
	
}	
	

	
/**
 * EnvConverter is class which helps getting names of environments in (String) 
 */
class EnvConverter extends StringConverter<Environment>{
	public Environment fromString(String str) {
		return new Environment(str);
	}
	
	public String toString(Environment env) {
		return env.getName();
	}
}



	
