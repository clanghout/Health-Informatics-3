package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

	/**
	 * Make a custom Dialog.
	 * @param location the location of the fxml file describing the dialog.
	 * @param name the name of the dialog window.
	 * @throws IOException
	 */
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

	/**
	 * return the fxml file to retrieve the controller used.
	 * @return the fxmlLoader for the dialog.
	 */
	public FXMLLoader getFxml() {
		return fxml;
	}

	/**
	 * Pop up the dialog.
	 */
	public void show() {
		dialog.show();
	}

	/**
	 * Sets the size of the dialog.
	 * @param width The width of the dialog
	 * @param height The height of the dialog
	 */
	public void setSize(double width, double height) {
		dialog.setHeight(height);
		dialog.setWidth(width);
	}

	/**
	 * Close the dialog.
	 */
	public void close() {
		dialog.close();
	}

	/**
	 * Create simple Information alert.
	 * @param title The title of the alert.
	 * @param message The body of the alert.
	 */
	public static void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
