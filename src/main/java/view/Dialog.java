package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Create a dialog.
 * Created by Chris on 2-6-2015.
 */
public abstract class Dialog {
	private Stage dialog;
	private FXMLLoader fxml;


	public Dialog(String location, String name) throws IOException {
		fxml = new FXMLLoader(getClass().getResource(location));
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

	/**
	 * Sets the size of the dialog.
	 * @param width The width of the dialog
	 * @param height The height of the dialog
	 */
	public void setSize(double width, double height) {
		dialog.setHeight(height);
		dialog.setWidth(width);
	}
}
