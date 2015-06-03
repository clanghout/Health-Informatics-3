package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Chris on 2-6-2015.
 */
public abstract class Dialog{
	private Stage dialog;


	public Dialog(String location, String name) throws IOException {
		System.out.println("location = " + location);
		Parent root = FXMLLoader.load(getClass().getResource(location));
		dialog = new Stage();
		dialog.setTitle(name);
		dialog.setScene(new Scene(root));
	}

	public void show() {
		dialog.show();
	}
}
