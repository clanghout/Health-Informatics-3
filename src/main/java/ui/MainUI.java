package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Creates and shows the graphical user interface.
 * @author Paul
 *
 */
public class MainUI extends Application {

	/**
	 * The text that is fetched from the fxml file.
	 */
	@FXML private Text buttonTest;
	
	public MainUI(){}
	
	/**
	 * Creates and shows the GUI by using the fxml file.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
		Scene scene = new Scene(root, 320, 240);

		primaryStage.setTitle("Analysis");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Called when the analyze button is pressed.
	 * @param event the event
	 */
	@FXML protected void handleAnalyzeAction(ActionEvent event) {
		buttonTest.setText("analyzed pressed");
	}
}
