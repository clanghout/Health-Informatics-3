package controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.input.reader.DataReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.data.DataModel;
import model.data.DataTable;
import model.output.DataTableWriter;

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
	private Button saveButton;

	@FXML
	private Label errorLabel;

	private MainUIController mainUIController;
	
	private Logger logger = Logger.getLogger("DataController");

	private File file;
	
	/**
	 * Creates a new TableViewController.
	 */
	public DataController() {
	}
	
	public void initialize(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
		saveButton.setDisable(true);
		errorLabel.setTextFill(Color.RED);
	}
	
	@FXML
	protected void handleImportButtonAction(ActionEvent event) {
		errorLabel.setText("");
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select Data Descriptor File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XML", "*.xml")
		);

		file = fileChooser.showOpenDialog(root.getScene().getWindow());
		if (file == null) {
			errorLabel.setText("ERROR: No file selected for import.");
		} else {
			fileNameField.setText(file.getAbsolutePath());
			read();
			saveButton.setDisable(false);
		}
	}

	private void read() {
		try {
			DataReader reader = new DataReader(file);
			DataModel model = reader.createDataModel();
			mainUIController.setModel(model);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error reading the file", e);
		}
	}

	@FXML
	protected void handleSaveButtonAction(ActionEvent event) {

	}
}
