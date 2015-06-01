package controllers;

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
	
	private MainUIController mainUIController;
	
	private Logger logger = Logger.getLogger("DataController");

	private File file;
	private DataTable out;
	
	/**
	 * Creates a new TableViewController.
	 */
	public DataController() {
	}
	
	public void initialize(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
	}
	
	@FXML
	protected void handleImportButtonAction(ActionEvent event) {
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
			// TODO: Handle no file selected with message.
		} else {
			fileNameField.setText(file.getAbsolutePath());
		}
	}

	@FXML
	protected void handleAnalyseButtonAction(ActionEvent event) {
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
		if (out != null) {
			FileChooser fileChooser = new FileChooser();

			fileChooser.setTitle("Select location to save output");
			fileChooser.setInitialDirectory(
					new File(System.getProperty("user.home"))
			);
			fileChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("TXT", "*.txt")
			);
			File temp = fileChooser.showSaveDialog(root.getScene().getWindow());

			DataTableWriter dmw = new DataTableWriter();
			try {
				dmw.write(out, temp, "\t");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error saving", e);
				// TODO: Show the error to the user.
			}
		}
	}
}
