package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
	private static final double WIZARD_DIALOG_HEIGHT = 640;

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
	 * @param model The DataModel
	 */
	public void setModel(DataModel model) {
		tableViewController.setDataModel(model);
		analysisController.setDataModel(model);
		visualizationController.setModel(model);
		visualizationController.initializeVisualisation();
	}

	@FXML
	protected void startWizard(ActionEvent actionEvent) {
		try {
			Dialog wizardDialog = new XMLCreationDialog();
			wizardDialog.setSize(WIZARD_DIALOG_WIDTH, WIZARD_DIALOG_HEIGHT);
			wizardDialog.show();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FXML error: " + e.getMessage());
		}
	}

}
