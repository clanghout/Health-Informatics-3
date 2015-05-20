package ui;

import input.file.DataFile;
import input.reader.XmlReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.data.DataTable;
import model.data.describer.ConstantDescriber;
import model.data.describer.RowValueDescriber;
import model.data.process.analysis.ConstraintAnalysis;
import model.data.process.analysis.DataAnalysis;
import model.data.process.analysis.operations.constraints.Constraint;
import model.data.process.analysis.operations.constraints.EqualityCheck;
import model.data.value.StringValue;
import model.reader.DataReader;
import output.DataTableWriter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the Data tab
 * <p />
 * Created by Boudewijn on 6-5-2015.
 */
public class DataController {
	@FXML
	private TextField fileNameField;
	@FXML
	private Parent root;

	private Logger logger = Logger.getLogger("DataController");

	private File file;
	private DataTable out;

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
			XmlReader reader = new XmlReader();
			reader.read(file);
			DataFile dataFile = reader.getDataFiles().get(0);
			DataReader dataReader = new DataReader();
			DataTable input = dataReader.readData(dataFile.getDataStream());

			Constraint constraint = new EqualityCheck<>(
					new RowValueDescriber<>(input.getColumns().get("time")),
					new ConstantDescriber<>(new StringValue("0803"))
			);
			DataAnalysis analysis = new ConstraintAnalysis(constraint);
			out = analysis.analyse(input);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error reading XML file", e);
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
