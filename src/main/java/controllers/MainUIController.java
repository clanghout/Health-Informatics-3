package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.logging.Logger;

/**
 * Controls the elements of the GUI.
 * @author Paul
 *
 */
public class MainUIController {

	private Logger logger = Logger.getLogger("MainUIController");

	/**
	 * Called when the "Quit" menubutton is pressed.
	 * @param event the event
	 */
	@FXML protected void handleQuitAction(ActionEvent event) {
		logger.info("Shutting down");
		System.exit(0);
	}
	
}
