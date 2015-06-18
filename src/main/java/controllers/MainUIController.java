package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import model.data.DataModel;
import view.SaveDialog;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Controls the elements of the GUI.
 *
 * @author Paul
 */
public class MainUIController {

	@FXML
	private TableViewController tableViewController;
	@FXML
	private DataController dataController;
	@FXML
	private AnalysisController analysisController;
	@FXML
	private VisualizationController visualizationController;
	@FXML
	private MenuItem save;
	private DataModel model;
	private Logger logger = Logger.getLogger("MainUIController");

	/**
	 * Initializes other controllers that depend on this controller.
	 */
	@FXML
	public void initialize() {
		save.setDisable(true);
		this.dataController.initialize(this);
	}

	/**
	 * Called when the "Quit" menubutton is pressed.
	 *
	 * @param event the event
	 */
	@FXML
	protected void handleQuitAction(ActionEvent event) {
		logger.info("Shutting down");
		Platform.exit();
	}

	@FXML
	protected void handleSaveAction() {
		SaveDialog saveDialog;
		try {
			saveDialog = new SaveDialog();
			saveDialog.show();
			SaveWizardController saveWizardController
					= saveDialog.getFxml().getController();
			saveWizardController.initializeView(model, saveDialog);
		} catch (IOException e) {
			//errorLabel.setText("ERROR: popup file is missing.");
		}
	}

	/**
	 * Sets the model for the other controllers that need the same DataModel.
	 *
	 * @param model The DataModel
	 */
	public void setModel(DataModel model) {
		this.model = model;
		save.setDisable(false);
		tableViewController.setDataModel(model);
		analysisController.setDataModel(model);
		visualizationController.setModel(model);
		visualizationController.initializeVisualisation();

	}

}
