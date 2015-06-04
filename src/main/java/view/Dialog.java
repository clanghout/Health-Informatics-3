package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;

/**
 * Create a dialog.
 * Created by Chris on 2-6-2015.
 */
public abstract class Dialog {
	private Stage dialog;
	private FXMLLoader fxml;
	private Logger logger = Logger.getLogger("VisualizationController");

	public Dialog(String location, String name) throws IOException {
		try {
			fxml = new FXMLLoader(getClass().getResource(location));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "resource not found");
		}
		Parent root = fxml.load();
		dialog = new Stage();
		dialog.setTitle(name);
		dialog.setScene(new Scene(root));
	}

	public FXMLLoader getFxml() {
		return fxml;
	}

	public void show() {
		dialog.show();
	}

	public void close() {
		dialog.close();
	}
}
