package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.data.DataModel;

import java.util.logging.Logger;

/**
 * Controls the elements of the GUI.
 * @author Paul
 *
 */
public class MainUIController {

	@FXML private TableViewController tableViewController;
	@FXML private DataController dataController;
	@FXML private AnalysisController analysisController;
	@FXML private VisualizationController visualizationController;
	private Logger logger = Logger.getLogger("MainUIController");

	/**
	 * Initializes other controllers that depend on this controller.
	 */
	@FXML public void initialize() {
		this.dataController.initialize(this);
	}
	
	/**
	 * Called when the "Quit" menubutton is pressed.
	 * @param event the event
	 */
	@FXML protected void handleQuitAction(ActionEvent event) {
		logger.info("Shutting down");
		Platform.exit();
	}
	
	/**
	 * Sets the model for the other controllers that need the same DataModel.
	 */
	public void setModelObservers() {
		tableViewController.setDataModelObserver();
		visualizationController.initializeVisualisation();

	}

}
