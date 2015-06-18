package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
	private boolean backgroundProcess = false;

	public boolean startBackgroundProcess(Task task) {
		if (backgroundProcess) {
			return false;
		}
		synchronized (MainUIController.class) {
			backgroundProcess = true;
			dataController.disableImport();
			analysisController.disableImport();
			System.out.println("disabe");
			new Thread(task).start();

			return true;
		}
	}

	public void endBackgroundProcess() {
		backgroundProcess = false;
		dataController.enableImport();
		analysisController.enableImport();

	}


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
	 * @param model The DataModel
	 */
	public void setModel(DataModel model) {
		tableViewController.setDataModel(model);
		analysisController.setDataModel(model);
		visualizationController.setModel(model);
		visualizationController.initializeVisualisation();

	}

}
