package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controls the elements of the GUI.
 * @author Paul
 *
 */
public class MainUIController {
	
	/**
	 * Called when the "Quit" menubutton is pressed.
	 * @param event the event
	 */
	@FXML protected void handleQuitAction(ActionEvent event) {
		System.exit(0);
	}
	
}
