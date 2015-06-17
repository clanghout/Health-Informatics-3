package controllers;

import javafx.event.ActionEvent;
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


/**
 * The controller for the Analysis tab.
 * <p>
 * Created by Boudewijn on 22-5-2015.
 */
public class AnalysisController {

	private DataModel model;

	@FXML
	private Parent root;
	@FXML
	private TextArea userscript;
	@FXML
	private Label errorLabel;
	@FXML
	private VBox errorBox;
	@FXML
	private Button execute, save, load, clear;

	private static final int ERROR_RANGE = 5;

	/**
	 * Sets the model that will be observed.
	 *
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
	}

	/**
	 * Initialization of the analysis tab.
	 */
	public void initialize() {
		execute.setMaxWidth(Double.MAX_VALUE);
		Double width = execute.getWidth();
		save.setMinWidth(width);
		load.setMinWidth(width);
		clear.setMinWidth(width);
	}

	@FXML
	protected void handleExecuteButtonAction(ActionEvent event) {
		Parser parser = new Parser();
		Label errorLabelExtra = new Label();
		try {
			DataProcess process = parser.parse(userscript.getText(), model);
			process.process();
			model.setUpdated();

		} catch (IllegalArgumentException e) {
			errorLabel.setText("ERROR: " + e.getMessage());
		} catch (ParseException e) {
			errorLabel.setText(e.getMessage());
			createParseExceptionMessage(e);
		} catch (UnsupportedOperationException e) {
			errorLabel.setText("ERROR: you are using an invalid operation");
		} catch (RuntimeException e) {
			errorLabel.setText("Runtime exception occurred");
			errorLabelExtra.setText(e.getLocalizedMessage() + " | " + e.getMessage());
		}
		errorBox.getChildren().add(errorLabelExtra);
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
}
