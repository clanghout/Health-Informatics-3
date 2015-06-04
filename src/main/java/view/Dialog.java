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
		System.out.println("location = " + location);
		fxml = new FXMLLoader(getClass().getResource(location));
		System.out.println("fxml = " + fxml);
		Parent root = fxml.load();
		System.out.println("root = " + root);
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
}
