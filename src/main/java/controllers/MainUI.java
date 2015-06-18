package controllers;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BackgroundProcesses.BackgroundProcessor;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

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
