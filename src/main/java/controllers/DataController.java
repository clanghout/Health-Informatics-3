package controllers;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.data.DataModel;
import view.SaveDialog;

import java.io.File;
import java.io.IOException;

/**
 * The controller for the Data tab.
 * <p />
 * Created by Boudewijn on 6-5-2015.
 */
public class DataController {
	@FXML
	private Button importButton;


	@FXML
	private TextField fileNameField;
	
	@FXML
	private Parent root;

	@FXML
	private Button saveButton;

	@FXML
	private Label errorLabel;

	private MainUIController mainUIController;

	private File file;
	private DataModel model;
	
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
		saveButton.setDisable(true);
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
			Reader reader = new Reader(file, mainUIController, errorLabel);
			reader.execute();
			errorLabel.setTextFill(Color.BLACK);
			errorLabel.setText("File Selected:");
			saveButton.setDisable(false);
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
	 * Handle the save button.
	 * Opens a save Dialog.
	 */
	@FXML
	protected void handleSaveButtonAction() {
		SaveDialog saveDialog;
		try {
			saveDialog = new SaveDialog();
			saveDialog.show();
			SaveWizardController saveWizardController
					= saveDialog.getFxml().getController();
			saveWizardController.initializeView(model, saveDialog);

		} catch (IOException e) {
			errorLabel.setText("ERROR: popup file is missing.");
		}


	}

	/**
	 * Disable the import button.
	 */
	public void disableImport() {
		importButton.setDisable(true);
	}

	/**
	 * Enable the import button.
	 */
	public void enableImport() {
		importButton.setDisable(false);
	}

	public Label getErrorLabel() {
		return errorLabel;
	}
}
