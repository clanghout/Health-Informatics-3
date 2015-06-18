package controllers;

import controllers.wizard.XmlWizardController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import model.data.DataModel;
import view.SaveDialog;
import java.io.IOException;
import view.Dialog;
import view.XMLCreationDialog;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the elements of the GUI.
 *
 * @author Paul
 */
public class MainUIController {

	@FXML
	private MenuItem save;
	private DataModel model;

	@FXML private TableViewController tableViewController;
	@FXML private DataController dataController;
	@FXML private AnalysisController analysisController;
	@FXML private VisualizationController visualizationController;
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
		try {
			SaveDialog saveDialog = new SaveDialog(model);
			saveDialog.show();
		} catch (IOException e) {
			e.printStackTrace();
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
