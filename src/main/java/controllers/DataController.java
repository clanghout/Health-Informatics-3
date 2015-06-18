package controllers;

import controllers.wizard.XmlWizardController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.data.DataModel;
import model.input.reader.DataReader;
import model.input.reader.XmlReader;
import view.Dialog;
import view.XMLCreationDialog;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the Data tab.
 * <p />
 * Created by Boudewijn on 6-5-2015.
 */
public class DataController {

	@FXML
	private TextField fileNameField;
	
	@FXML
	private Parent root;

	@FXML
	private Button xmlWizardButton;

	@FXML
	private Label errorLabel;

	private MainUIController mainUIController;
	
	private Logger logger = Logger.getLogger("DataController");

	private File file;
	private DataModel model;

	private static final double WIZARD_DIALOG_WIDTH = 1000;
	private static final double WIZARD_DIALOG_HEIGHT = 800;


	/**
	 * Creates a new TableViewController.
	 */
	public DataController() {
	}

	/**
	 * Initialization of the controller.
	 * @param mainUIController the main UI controller.
	 */
	public void initialize(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
		errorLabel.setText("Import data");
	}

	/**
	 * Handler for the import button.
	 * Select a file and read it.
	 */
	@FXML
	protected void handleImportButtonAction() {
		errorLabel.setText("");
		file = chooseFile();
		if (file == null) {
			errorLabel.setTextFill(Color.RED);
			errorLabel.setText("ERROR: No file selected for import.");
		} else {
			fileNameField.setText(file.getAbsolutePath());
			read();
			errorLabel.setTextFill(Color.BLACK);
			errorLabel.setText("File Selected:");
		}
	}

	@FXML
	protected void handleXMLWizardButtonAction() {
		try {
			Dialog wizardDialog = new XMLCreationDialog();
			XmlWizardController wizardController =
					wizardDialog.getFxml().getController();
			wizardController.initializeView(mainUIController, wizardDialog);
			wizardDialog.setSize(WIZARD_DIALOG_WIDTH, WIZARD_DIALOG_HEIGHT);
			wizardDialog.show();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FXML error: " + e.getMessage());
		}
	}

	/**
	 * Open a fileChooser to select the location of the xml file.
	 * @return File object containing the xml file.
	 */
	private File chooseFile() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select Data Descriptor File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XML", "*.xml")
		);

		return fileChooser.showOpenDialog(root.getScene().getWindow());
	}

	/**
	 * Read the data and set the model in the mainUIController.
	 */
	private void read() {
		try {
			DataReader reader = new DataReader(new XmlReader());
			reader.read(file);
			model = reader.createDataModel();
			mainUIController.setModel(model);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "An error occurred while reading the file", e);
		}
	}
}
