package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.data.DataModel;
import model.data.process.analysis.ConstraintAnalysis;
import model.data.process.analysis.DataAnalysis;
import model.data.process.analysis.constraints.Constraint;
import model.data.process.analysis.constraints.EqualityCheck;
import model.data.value.StringValue;
import model.reader.DataReader;
import output.DataModelWriter;
import xml.DataFile;
import xml.XmlReader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the Data tab
 *
 * Created by Boudewijn on 6-5-2015.
 */
public class DataController {
	@FXML private TextField fileNameField;
	@FXML private Parent root;

	private Logger logger = Logger.getLogger("DataController");

	private File file;

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
		fileNameField.setText(file.getAbsolutePath());
	}

	@FXML
	protected void handleAnalyseButtonAction(ActionEvent event) {
		try {
			XmlReader reader = new XmlReader(file);
			DataFile dataFile = reader.getDataFiles().get(0);
			DataReader dataReader = new DataReader();
			DataModel input = dataReader.readData(dataFile.filterHeader());

			Constraint constraint = new EqualityCheck(
					input.getColumns().get("time"), new StringValue("0803")
			);

			DataAnalysis analysis = new ConstraintAnalysis(constraint);
			DataModel out = analysis.analyse(input);
			DataModelWriter.write(out, "out/dingen.txt", "\t");
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error reading XML file", e);
		}
	}

}
