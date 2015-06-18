package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.BackgroundProcesses.Analyse;
import model.BackgroundProcesses.BackgroundProcessor;
import model.data.ProgramModel;
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

	private DataModel model = ProgramModel.getDataModel();;

	@FXML
	private Parent root;
	@FXML
	private TextArea userscript;
	@FXML
	volatile private Label errorLabel;
	@FXML
	volatile private VBox errorBox;




	@FXML
	protected void handleExecuteButtonAction(ActionEvent event) {

		try {
			errorBox.getChildren().clear();
			errorLabel.setText("");
			Analyse analyse = new Analyse(model, userscript.getText(), errorLabel, errorBox);
			BackgroundProcessor.getQueue().add(analyse);
		} catch (IllegalStateException e) {
			errorLabel.setText("Already executing an analysis");
		}

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
	 * Clear all the error labels.
	 */
	private void emptyLabels() {
		errorLabel.setText("");
		errorBox.getChildren().clear();
	}
}
