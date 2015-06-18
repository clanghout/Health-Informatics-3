package controllers;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.exceptions.ParseException;
import model.language.Parser;
import model.data.DataModel;
import model.process.DataProcess;
import org.parboiled.buffers.InputBuffer;
import org.parboiled.errors.ParseError;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The controller for the Analysis tab.
 * <p>
 * Created by Boudewijn on 22-5-2015.
 */
public class AnalysisController {

	private DataModel model;

	@FXML
	private Button executeButton;
	@FXML
	private Parent root;
	@FXML
	private TextArea userscript;
	@FXML
	private Label errorLabel;
	@FXML
	private VBox errorBox;
	private MainUIController mainUIController;
	private Logger logger = Logger.getLogger("AnalysisController");
	private static final int ERROR_RANGE = 5;

	/**
	 * Sets the model that will be observed.
	 *
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
	}

	public void setMainUIController(MainUIController main) {
		this.mainUIController = main;
	}

	@FXML
	protected void handleExecuteButtonAction(ActionEvent event) {
			emptyLabels();
			Task task = createTask();
			setFailed(task);
			setSucceed(task);

			mainUIController.startBackgroundProcess(task, "Analysing");
		}

	/**
	 * Set the callback functio for a task that succeeds.
	 * @param task task that must get the callback function.
	 */
	private void setSucceed(Task task) {
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override public void handle(WorkerStateEvent t) {
				model.setUpdated();
				logger.info("Analysis complete");
				mainUIController.endBackgroundProcess(true);
			}
		});
	}

	/**
	 * Create a task for the analysis.
	 * @return a new task that can perform an analysis.
	 */
	private Task createTask() {
		return new Task() {
			@Override protected Integer call() throws Exception {
				Parser parser = new Parser();
				DataProcess process = parser.parse(userscript.getText(), model);
				process.process();

				return null;
			}
		};
	}


	@FXML
	protected void handleLoadButtonAction() {
		userscript.setText("");
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select file to load");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("TXT", "*.txt")
		);

		File load =  fileChooser.showOpenDialog(root.getScene().getWindow());
		String script = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(load))) {
			String line;
			script = reader.readLine();
			while ((line = reader.readLine()) != null) {
				script += "\n" + line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		userscript.setText(script);
	}

	@FXML
	protected void handleSaveButtonAction() {
		emptyLabels();
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Save your user script");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter(
						"TXT", "*.txt")
		);
		File saveLoc = fileChooser.showSaveDialog(root.getScene().getWindow());
		try (PrintWriter writer = new PrintWriter(saveLoc, "UTF-8")) {
			userscript.getParagraphs().forEach(writer::println);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			errorLabel.setText("Something went wrong while saving the file");
		}

	}

	@FXML
	protected void handleClearButtonAction() {
		userscript.setText("");
	}

	/**
	 * Create a label with error message for every parse error.
	 *
	 * @param e teh Exception containing the parse errors
	 */
	private void createParseExceptionMessage(ParseException e) {
		List<ParseError> parseErrors = e.getParseErrors();
		for (ParseError error : parseErrors) {
			InputBuffer buffer = error.getInputBuffer();
			Label label = new Label();
			label.setMaxWidth(Double.MAX_VALUE);
			String beforeError = buffer.extract(0, error.getStartIndex());
			int lineCount = beforeError.length() - beforeError.replace("\n", "").length();
			String errorChar =
					buffer.extract(error.getStartIndex() - ERROR_RANGE,
							error.getEndIndex() + ERROR_RANGE);
			label.setText("Error at line "
					+ lineCount
					+ "; near \""
					+ errorChar + "\"");
			errorBox.getChildren().add(label);
		}
	}

	/**
	 * Clear all the error labels.
	 */
	private void emptyLabels() {
		errorLabel.setText("");
		errorBox.getChildren().clear();
	}

	/**
	 * Enable the execute button.
	 */
	public void enableImport() {
		executeButton.setDisable(false);
	}

	/**
	 * Disable the execute button.
	 */
	public void disableImport() {
		executeButton.setDisable(true);
	}

	/**
	 * Set the callback function when the task fails.
	 * This create the error messages.
	 * @task the task that must get the callback function.
	 */
	public void setFailed(Task task) {
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override public void handle(WorkerStateEvent t) {
				Label errorLabelExtra = new Label();
				mainUIController.endBackgroundProcess(false);
				logger.info("Error occured during the analysis");
				try {
					throw task.getException();
				} catch (IllegalArgumentException e) {
					errorLabel.setText("ERROR: " + e.getMessage());
				} catch (ParseException e) {
					errorLabel.setText(e.getMessage());
					createParseExceptionMessage(e);
				} catch (UnsupportedOperationException e) {
					errorLabel.setText("ERROR: you are using an invalid operation");
				} catch (RuntimeException e) {
					errorLabel.setText("Runtime exception occurred");
					errorLabelExtra.setText(e.getMessage());
				} catch (Throwable e) {
					errorLabel.setText("An unexpected error occurred");
					logger.log(Level.WARNING, "An error occurred while processing the analysis",
							e.toString());
				}
				errorBox.getChildren().add(errorLabelExtra);
			}
		});
	}
}
