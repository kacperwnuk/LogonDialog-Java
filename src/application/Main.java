package application;
	
import java.util.Optional;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main class
 * @author Kacper Wnuk 4I1 Informatyka
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Optional<Pair<Environment, String>> result = (new LogonDialog("Logowanie",  "Logowanie do systemu STYLEman")).showAndWait();
		if(result.isPresent()) {
			System.out.println("Zalogowano pomyœlnie");
		}
		else{
			System.out.println("Logowanie zakonczone niepowodzeniem");
		}

	}
	
	@Override
	public void stop()
	{
		System.out.println("Program finished.");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}












