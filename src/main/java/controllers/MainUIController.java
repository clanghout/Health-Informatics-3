package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
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
	@FXML private ProgressIndicator indicator;
	@FXML private Label progressLabel;

	private Logger logger = Logger.getLogger("MainUIController");
	private boolean backgroundProcess = false;

	public boolean startBackgroundProcess(Task task, String process) {
		if (backgroundProcess) {
			return false;
		}
		synchronized (MainUIController.class) {
			indicator.setVisible(true);
			progressLabel.setTextFill(Color.BLACK);
			progressLabel.setText(process);
			backgroundProcess = true;
			dataController.disableImport();
			analysisController.disableImport();
			logger.info("Start process");
			new Thread(task).start();

			return true;
		}
	}

	public void endBackgroundProcess(boolean succes) {
		logger.info("End process");
		backgroundProcess = false;
		dataController.enableImport();
		analysisController.enableImport();
		indicator.setVisible(false);

		if (succes) {
			progressLabel.setTextFill(Color.BLACK);
			progressLabel.setText("Done");
		} else {
			progressLabel.setTextFill(Color.RED);
			progressLabel.setText("Error");
		}

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
		analysisController.setMainUIController(this);
		visualizationController.setModel(model);
		visualizationController.initializeVisualisation();

	}

}
