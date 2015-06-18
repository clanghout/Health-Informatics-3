package controllers;

import controllers.wizard.XmlWizardController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import model.data.DataModel;
import view.Dialog;
import view.XMLCreationDialog;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the elements of the GUI.
 * @author Paul
 *
 */
public class MainUIController {

	private static final double WIZARD_DIALOG_WIDTH = 1000;
	private static final double WIZARD_DIALOG_HEIGHT = 800;

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

			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();


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

	@FXML
	protected void startWizard(ActionEvent actionEvent) {
		try {
			Dialog wizardDialog = new XMLCreationDialog();
			XmlWizardController wizardController =
					wizardDialog.getFxml().getController();
			wizardController.initializeView(this, wizardDialog, dataController.getErrorLabel());
			wizardDialog.setSize(WIZARD_DIALOG_WIDTH, WIZARD_DIALOG_HEIGHT);
			wizardDialog.show();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FXML error: " + e.getMessage());
		}
	}

}
