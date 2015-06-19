package controllers.visualizations;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * Created by Chris on 19-6-2015.
 */
public abstract class GraphImageController {

	public abstract void initialize();

	/**
	 * Set the message to an error label.
	 *
	 * @param label   the label wich will show the error.
	 * @param message the message for in the label.
	 */
	public void setErrorLabel(Label label, String message) {
		label.setTextFill(Color.RED);
		label.setText(message);
	}
}
