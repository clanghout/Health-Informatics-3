package controllers.wizard;

import controllers.MainUIController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.data.DataModel;
import model.data.value.DataValue;
import model.exceptions.DataFileNotRecognizedException;
import model.input.file.DataFile;
import model.input.reader.XmlReader;
import model.output.XmlWriter;
import org.xml.sax.SAXException;
import view.Dialog;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for the wizard to make the user able to create a datafile specifying xml.
 *
 * @author Paul.
 */
public class XmlWizardController {

	private Logger logger = Logger.getLogger("XmlWizardController");

	@FXML private Button metaApply;
	@FXML private CheckBox addmetacheck;
	@FXML private TextField endLine;
	@FXML private TextField startLine;
	@FXML private CheckBox hasFirstRowHeader;
	@FXML private ComboBox metacolumntype;
	@FXML private TextField metacolumnName;
	@FXML private ComboBox columntype;
	@FXML private TextField columnName;
	@FXML private TableView datacolumns;
	@FXML private ListView<DataFile> datafiles;
	@FXML private Parent root;
	@FXML private TextField fileselectfield;

	private Dialog dialog;
	private MainUIController mainUIcontroller;

	private DataFile selectedFile;

	private final ObservableList typesSelect = FXCollections.observableArrayList(
			"string", "int", "float", "datetime", "date", "time"
	);

	private final ChangeListener<DataFile> listener = (ov, oldValue, newValue) -> {
		if (!(newValue == null)) {
			selectedFile = newValue;
			fillElements();
		}
	};

	private final ChangeListener<String> startLineChangeListener = (ov, oldValue, newValue) -> {
		if (newValue != null && selectedFile != null) {
			selectedFile.setStartLine(Integer.parseInt(newValue));
		}
	};

	private final ChangeListener<String> endLineChangeListener = (ov, oldValue, newValue) -> {
		if (newValue != null && selectedFile != null) {
			selectedFile.setEndLine(Integer.parseInt(newValue));
		}
	};
	private final ChangeListener<String> metacolumnNameListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(selectedFile.getMetaDataColumnName()))) {

			metaApply.setDisable(false);
		} else {
			metaApply.setDisable(true);
		}
	};
	private final ChangeListener<String> metacolumntypeListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& selectedFile.getMetaDataType() != null
				&& !(newValue.equals(DataFile.getStringColumnType(selectedFile.getMetaDataType())))) {

			metaApply.setDisable(false);
		}
	};

	/**
	 * Initializes the controller by filling the static content of elements in the view.
	 */
	public void initialize() {
		startLine.textProperty().addListener(startLineChangeListener);
		endLine.textProperty().addListener(endLineChangeListener);
		metacolumntype.getSelectionModel().select("string");
		metacolumnName.setDisable(true);
		metacolumntype.setDisable(true);
		metaApply.setDisable(true);
		metacolumnName.textProperty().addListener(metacolumnNameListener);
		metacolumntype.valueProperty().addListener(metacolumntypeListener);
		this.datafiles.getSelectionModel().selectedItemProperty().addListener(listener);
		datacolumns.getColumns().add(createColumn(0, "Column name"));
		datacolumns.getColumns().add(createColumn(1, "Type"));

		metacolumntype.setItems(typesSelect);
		columntype.setItems(typesSelect);
	}

	public void initializeView(MainUIController mainUIController, Dialog dialog) {
		this.dialog = dialog;
		this.mainUIcontroller = mainUIController;
	}

	@FXML
	public void selectFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Plain text file", "*.txt"),
				new FileChooser.ExtensionFilter("MS Excel xls file", "*.xls"),
				new FileChooser.ExtensionFilter("MS Excel xlsx file", "*.xlsx")
		);
		fileChooser.setTitle("Select Data File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);

		File file = fileChooser.showOpenDialog(root.getScene().getWindow());
		String type = getTypeByExtension(
				fileChooser.getSelectedExtensionFilter().getExtensions().get(0));
		addDataFile(file.getPath(), type);
		fileselectfield.setText(file.getPath());
	}

	private String getTypeByExtension(String extension) {
		switch (extension) {
			case "*.txt":
				return "plaintext";
			case "*.xls":
				return "xls";
			case "*.xlsx":
				return "xlsx";
			default: throw new DataFileNotRecognizedException(
					"The selected extension is not recognized");
		}
	}

	private void fillElements() {
		if (selectedFile.hasFirstRowAsHeader()) {
			hasFirstRowHeader.setSelected(true);
		} else {
			hasFirstRowHeader.setSelected(false);
		}
		fillMetaElements();
		startLine.setText(String.valueOf(selectedFile.getStartLine()));
		endLine.setText(String.valueOf(selectedFile.getEndLine()));

		updateColumnsView();
	}

	private void fillMetaElements() {
		if (selectedFile.hasMetaData()
				&& selectedFile.getMetaDataType() != null
				&& selectedFile.getMetaDataColumnName() != null) {

			addmetacheck.setSelected(true);
			metacolumnName.setDisable(false);
			metacolumntype.setDisable(false);
			metacolumnName.setText(selectedFile.getMetaDataColumnName());
			metacolumntype.getSelectionModel().select(
					DataFile.getStringColumnType(selectedFile.getMetaDataType()));
		} else {
			addmetacheck.setSelected(false);
			metacolumnName.setDisable(true);
			metacolumnName.setText("");
			metacolumntype.setDisable(true);
			metaApply.setDisable(true);
		}
	}

	private TableColumn<ObservableList<StringProperty>, String> createColumn(int index,
																			 String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column
				= new TableColumn<>(columnTitle);
		column.setCellValueFactory(
				cellDataFeatures -> cellDataFeatures.getValue().get(index));
		return column;
	}

	private void addDataFile(String path, String type) {
		DataFile file = DataFile.createDataFile(path, type);
		datafiles.getItems().add(file);
	}

	private void updateColumnsView() {
		datacolumns.getItems().clear();
		Map<String, Class<? extends DataValue>> columns = selectedFile.getColumns();

		for (Map.Entry<String, Class<? extends DataValue>> entry : columns.entrySet()) {
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			row.add(new SimpleStringProperty(entry.getKey()));
			row.add(new SimpleStringProperty(DataFile.getStringColumnType(entry.getValue())));
			datacolumns.getItems().add(row);
		}
	}

	@FXML
	public void addColumnRow(ActionEvent actionEvent) {
		if (selectedFile != null) {
			ObservableList<StringProperty> row = FXCollections.observableArrayList();

			String colName = columnName.getText();
			String colType = (String) columntype.getValue();

			row.add(new SimpleStringProperty(colName));
			row.add(new SimpleStringProperty(colType));

			selectedFile.getColumns().put(colName, DataFile.getColumnType(colType));
			updateColumnsView();
		}
	}

	private List<DataFile> createDataFiles() {
		List res = new ArrayList();
		for (DataFile file : datafiles.getItems()) {
			res.add(file);
		}
		return res;
	}

	@FXML
	public void createXml(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		fileChooser.setTitle("Save as");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		File file = fileChooser.showSaveDialog(root.getScene().getWindow());
		writeXmlToFile(file);
		try {
			mainUIcontroller.setModel(createModel());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error creating datamodel " + e.getMessage());
		}
	}

	private DataModel createModel() throws IOException {
		DataModel res = new DataModel();
		for (DataFile datafile: datafiles.getItems()) {
			res.add(datafile.createDataTable());
		}
		return res;
	}

	private void writeXmlToFile(File file) {
		XmlWriter writer = new XmlWriter(createDataFiles());
		writer.write(file);
	}

	@FXML
	public void removeColumnRow(ActionEvent actionEvent) {
		String selectedColumn =
				((ObservableList<StringProperty>) datacolumns.
						getSelectionModel().getSelectedItem()).get(0).getValue();

		selectedFile.getColumns().remove(selectedColumn);
		updateColumnsView();
	}

	@FXML
	public void removeDataFile(ActionEvent actionEvent) {
		List items = datafiles.getItems();
		items.remove(datafiles.getSelectionModel().getSelectedItem());
		selectedFile = null;
	}

	@FXML
	public void handleFirstRowHeaderCheckbox(ActionEvent actionEvent) {
		if (selectedFile != null) {
			selectedFile.setFirstRowAsHeader(hasFirstRowHeader.isSelected());
		}
	}

	@FXML
	public void loadXml(ActionEvent actionEvent) {
		try {
			XmlReader reader = new XmlReader();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Data Descriptor File");
			fileChooser.setInitialDirectory(
					new File(System.getProperty("user.home"))
			);
			fileChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("XML", "*.xml")
			);

			File file = fileChooser.showOpenDialog(root.getScene().getWindow());
			reader.read(file);
			datafiles.getItems().clear();
			datafiles.getItems().addAll(reader.getDataFiles());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.log(Level.SEVERE, "Error when reading xml file: " + e.getMessage() , e);
		}
	}

	@FXML
	public void addMetaData(ActionEvent actionEvent) {
		if (addmetacheck.isSelected()) {
			selectedFile.setHasMetaData(true);
			metacolumnName.setDisable(false);
			metacolumntype.setDisable(false);
		} else {
			selectedFile.setHasMetaData(false);
			metacolumnName.setDisable(true);
			metacolumntype.setDisable(true);
			metaApply.setDisable(true);
		}
	}

	@FXML
	public void setMetaData(ActionEvent actionEvent) {
		selectedFile.setMetaDataColumnName(metacolumnName.getText());
		selectedFile.setMetaDataType(DataFile.getColumnType(
				(String) metacolumntype.getSelectionModel().getSelectedItem()));
		metaApply.setDisable(true);
	}

	public void handleCancelButton(ActionEvent actionEvent) {
		dialog.close();
	}
}
