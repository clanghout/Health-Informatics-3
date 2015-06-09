package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Creates and shows the graphical user interface.
 * @author Paul
 *
 */
public class MainUI extends Application {

	public MainUI() { }
	
	/**
	 * Creates and shows the GUI by using the fxml file.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setTitle("Analysis");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
