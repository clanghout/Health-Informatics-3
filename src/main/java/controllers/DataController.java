package controllers;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.input.reader.DataReader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.data.DataModel;
import model.process.DataProcess;
import view.SaveDialog;

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
	
	private Logger logger = Logger.getLogger("DataController");

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
			read();
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
	 * Read the data and set the model in the mainUIController.
	 */
	private void read() {
		try {
			errorLabel.setText("");
			Task task = createTask();
			setHandlers(task);

			if (!mainUIController.startBackgroundProcess(task)) {
				errorLabel.setText("An operation is already running in the background");
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "An error occurred while reading the file", e);
		}
	}

	private void setHandlers(Task task) {

		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override public void handle(WorkerStateEvent t) {
				Throwable exception = task.getException();
				logger.log(Level.WARNING, "An error occurred while reading the file"
								+ exception.getClass().getName() + " -> "
								+ exception.getMessage(),
						exception.getCause());
				mainUIController.endBackgroundProcess();
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("An error occurred while reading the file");
			}
		});

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override public void handle(WorkerStateEvent t) {
				mainUIController.setModel(model);
				mainUIController.endBackgroundProcess();
			}
		});
	}

	private Task createTask() {
		return new Task() {
			@Override protected Integer call() throws Exception {
				DataReader reader = new DataReader(file);
				model = reader.createDataModel();

				return null;
			}
		};
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

	public void disableImport() {
		importButton.setDisable(true);
	}

	public void enableImport() {
		importButton.setDisable(false);
	}
}
