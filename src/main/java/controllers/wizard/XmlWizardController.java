package controllers.wizard;

import controllers.MainUIController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.data.DataModel;
import model.exceptions.DataFileNotRecognizedException;
import model.input.file.ColumnInfo;
import model.input.file.DataFile;
import model.input.file.PlainTextFile;
import model.input.reader.XmlReader;
import model.output.XmlWriter;
import org.xml.sax.SAXException;
import view.Dialog;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for the wizard to make the user able to create a datafile specifying xml.
 *
 * @author Paul.
 */
public class XmlWizardController {

	private Logger logger = Logger.getLogger("XmlWizardController");
	@FXML private Button apply;

	@FXML private CheckBox addmetacheck;
	@FXML private TextField endLine;
	@FXML private TextField startLine;
	@FXML private CheckBox hasFirstRowHeader;

	@FXML private TextField delimiter;
	@FXML private ComboBox metacolumntype;
	@FXML private TextField metacolumnName;
	@FXML private TextField metacolumnformat;
	@FXML private TextField metacolumnvalue;

	@FXML private ComboBox columntype;
	@FXML private TextField columnName;
	@FXML private TextField columnFormat;
	@FXML private TableView datacolumns;

	@FXML private ListView<DataFile> datafiles;
	@FXML private Parent root;
	@FXML private TextField fileselectfield;

	private TableColumn[] columns =
			{createColumn(0, "Column name"),
			createColumn(1, "Type"),
			createColumn(2, "Format")};

	private Dialog dialog;
	private MainUIController mainUIcontroller;

	private DataFile selectedFile;

	private final ObservableList typesSelect = FXCollections.observableArrayList(
			"string", "int", "float", "bool", "datetime", "date", "time"
	);

	private final ChangeListener<DataFile> listener = (ov, oldValue, newValue) -> {
		if (!(newValue == null)) {
			selectedFile = newValue;
			disableAll(false);
			fillElements();
			apply.setDisable(true);
		} else {
			disableAll(true);
		}
	};

	private final ChangeListener<String> startLineChangeListener = (ov, oldValue, newValue) -> {
		if (newValue != null && selectedFile != null) {
			if (!(newValue.equals(String.valueOf(selectedFile.getStartLine())))) {
				apply.setDisable(false);
			}
		}
	};

	private final ChangeListener<String> endLineChangeListener = (ov, oldValue, newValue) -> {
		if (newValue != null && selectedFile != null) {
			if (!(newValue.equals(String.valueOf(selectedFile.getEndLine())))) {
				apply.setDisable(false);
			}
		}
	};

	private final ChangeListener<String> metacolumnNameListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(selectedFile.getMetaDataColumnName()))) {

			apply.setDisable(false);
		}
	};

	private final ChangeListener<String> metacolumntypeListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& selectedFile.getMetaDataType() != null
				&& !(newValue.equals(DataFile.getStringColumnType(
					selectedFile.getMetaDataType())))) {

			apply.setDisable(false);
		}
	};

	private final ChangeListener<String> columnTypeListener = ((observable, oldValue, newValue) -> {
		logger.info("Column type selected: " + newValue);
		if (selectedFile != null) {
			if (newValue.equals("date")
					|| newValue.equals("datetime")
					|| newValue.equals("time")) {
				columnFormat.setDisable(false);
			} else {
				columnFormat.setText("");
				columnFormat.setDisable(true);
			}
		}
	});

	private final ChangeListener<String> metacolumnvalueListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(selectedFile.getMetaDataValue()))) {

			apply.setDisable(false);
		}
	};

	private final ChangeListener<String> metacolumnformatListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(selectedFile.getMetaDataColumnName()))) {

			apply.setDisable(false);
		}
	};

	private final ChangeListener<String> delimiterListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(((PlainTextFile) selectedFile).getDelimiter()))) {

			apply.setDisable(false);
		}
	};

	private final ChangeListener<String> filePathListener = (observable, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(selectedFile.getPath()))) {
			apply.setDisable(false);
		}
	};

	private ListChangeListener datacolumnsListener = new ListChangeListener() {
		private boolean suspended;

		@Override
		public void onChanged(Change c) {
			c.next();
			if (c.wasReplaced() && !suspended) {
				suspended = true;
				datacolumns.getColumns().setAll(columns);
				suspended = false;
			}
		}
	};

	/**
	 * Initializes the controller by filling the static content of elements in the view.
	 */
	public void initialize() {
		startLine.textProperty().addListener(startLineChangeListener);
		endLine.textProperty().addListener(endLineChangeListener);
		delimiter.textProperty().addListener(delimiterListener);
		metacolumntype.getSelectionModel().select("string");
		columntype.getSelectionModel().select("string");
		columntype.valueProperty().addListener(columnTypeListener);
		datacolumns.getColumns().addListener(datacolumnsListener);
		fileselectfield.textProperty().addListener(filePathListener);

		disableAll(true);

		metacolumnName.textProperty().addListener(metacolumnNameListener);
		metacolumntype.valueProperty().addListener(metacolumntypeListener);
		metacolumnvalue.textProperty().addListener(metacolumnvalueListener);
		metacolumnformat.textProperty().addListener(metacolumnformatListener);
		this.datafiles.getSelectionModel().selectedItemProperty().addListener(listener);
		datacolumns.getColumns().setAll(columns);

		metacolumntype.setItems(typesSelect);
		columntype.setItems(typesSelect);
	}

	/**
	 * Initializes the view with a dialog and the referring MainUIController.
	 * @param mainUIController The MainUIController
	 * @param dialog The dialog that is shown to the user
	 */
	public void initializeView(MainUIController mainUIController, Dialog dialog) {
		this.dialog = dialog;
		this.mainUIcontroller = mainUIController;
	}

	/**
	 * Creates a dialog for the user to choose a DataFile.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void selectFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All supported files",
						Arrays.<String>asList("*.txt", "*.xls", "*.xlsx")),
				new FileChooser.ExtensionFilter("Plain text file", "*.txt"),
				new FileChooser.ExtensionFilter("MS Excel xls file", "*.xls"),
				new FileChooser.ExtensionFilter("MS Excel xlsx file", "*.xlsx")
		);
		fileChooser.setTitle("Select Data File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);

		File file = fileChooser.showOpenDialog(root.getScene().getWindow());
		if (file != null) {
			int startExt = file.getName().lastIndexOf(".");
			int endExt = file.getName().length();
			String type = getTypeByExtension(
					file.getName().substring(
							startExt, endExt));
			addDataFile(file.getPath(), type);
		}
	}

	private String getTypeByExtension(String extension) {
		switch (extension) {
			case ".txt":
				return "plaintext";
			case ".xls":
				return "xls";
			case ".xlsx":
				return "xlsx";
			default: throw new DataFileNotRecognizedException(
					"The selected extension is not recognized");
		}
	}

	private void fillElements() {
		if (selectedFile.hasFirstRowAsHeader()) {
			hasFirstRowHeader.setSelected(true);
			((TableColumn) datacolumns.getColumns().get(0)).setVisible(false);
		} else {
			hasFirstRowHeader.setSelected(false);
			((TableColumn) datacolumns.getColumns().get(0)).setVisible(true);
		}
		fillMetaElements();
		fileselectfield.setText(selectedFile.getPath());

		if (selectedFile instanceof PlainTextFile) {
			fillPlainTextElements();
		} else {
			disablePlaintextElements();
		}
		updateColumnsView();
	}

	private void disablePlaintextElements() {
		startLine.setDisable(true);
		endLine.setDisable(true);
		delimiter.setDisable(true);
	}

	private void fillPlainTextElements() {
		PlainTextFile plainTextFile = (PlainTextFile) selectedFile;
		startLine.setText(String.valueOf(plainTextFile.getStartLine()));
		endLine.setText(String.valueOf(plainTextFile.getEndLine()));
		delimiter.setText(String.valueOf(plainTextFile.getDelimiter()));
	}

	private void fillMetaElements() {
		if (selectedFile.hasMetaData()
				&& selectedFile.getMetaDataType() != null
				&& selectedFile.getMetaDataColumnName() != null) {
			enableMeta();
		} else {
			disableMeta();
		}
	}

	private void enableMeta() {
		logger.info(String.format("enable metadata: %s, %s with value %s and format %s",
				selectedFile.getMetaDataColumnName(), selectedFile.getMetaDataType(),
				selectedFile.getMetaDataValue().getValue(), selectedFile.getMetaDataFormat()));

		addmetacheck.setSelected(true);
		metacolumnName.setDisable(false);
		metacolumntype.setDisable(false);
		metacolumnvalue.setDisable(false);
		metacolumnName.setText(selectedFile.getMetaDataColumnName());

		metacolumntype.getSelectionModel().select(
				DataFile.getStringColumnType(selectedFile.getMetaDataType()));
		metacolumnformat.setText(selectedFile.getMetaDataFormat());

		if (selectedFile.getMetaDataValue().getValue() != null) {
			metacolumnvalue.setText(selectedFile.getMetaDataValue().getValue().toString());
		}
	}

	private void disableMeta() {
		addmetacheck.setSelected(false);
		metacolumnName.setDisable(true);
		metacolumnName.setText("");

		metacolumntype.setDisable(true);

		metacolumnformat.setDisable(true);
		metacolumnformat.setText("");

		metacolumnvalue.setDisable(true);
		metacolumnvalue.setText("");
	}

	private TableColumn<ObservableList<StringProperty>, String> createColumn(int index,
																			 String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column
				= new TableColumn<>(columnTitle);
		column.setCellValueFactory(
				cellDataFeatures -> cellDataFeatures.getValue().get(index));
		column.setSortable(false);
		return column;
	}

	private void addDataFile(String path, String type) {
		DataFile file = DataFile.createDataFile(path, type);
		datafiles.getItems().add(file);
	}

	private void setDataFilePath() {
		selectedFile.setPath(fileselectfield.getText());
	}

	private void updateColumnsView() {
		datacolumns.getItems().clear();
		List<ColumnInfo> columns = selectedFile.getColumns();

		for (ColumnInfo columnInfo : columns) {
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			row.add(new SimpleStringProperty(columnInfo.getName()));
			row.add(new SimpleStringProperty(DataFile.getStringColumnType(columnInfo.getType())));
			row.add(new SimpleStringProperty(columnInfo.getFormat()));
			datacolumns.getItems().add(row);
		}
	}

	/**
	 * Adds a row describing a column to the table of columns.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void addColumnRow(ActionEvent actionEvent) {
		if (selectedFile != null) {

			String colName = columnName.getText();
			String colType = (String) columntype.getValue();
			String format = columnFormat.getText();

			selectedFile.addColumnInfo(
					new ColumnInfo(colName, DataFile.getColumnType(colType), format));
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

	/**
	 * Creates a dialog for the user to choose the destination where to write the xml file to.
	 * @param actionEvent JavaFX event
	 */
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
			dialog.close();
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

	/**
	 * Removes the selected row from the table of columns.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void removeColumnRow(ActionEvent actionEvent) {
		if (selectedFile != null) {
			int selectedIndex = datacolumns.getItems().indexOf(
					datacolumns.getSelectionModel().getSelectedItem());
			selectedFile.getColumns().remove(selectedIndex);
			updateColumnsView();
		}
	}

	/**
	 * Removes the selected datafile in the listview of datafiles.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void removeDataFile(ActionEvent actionEvent) {
		List items = datafiles.getItems();
		items.remove(datafiles.getSelectionModel().getSelectedItem());
		selectedFile = null;
	}

	/**
	 * Changes the selected file to use the first row as a header in the table
	 * when the checkbox is selected.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void handleFirstRowHeaderCheckbox(ActionEvent actionEvent) {
		if (selectedFile != null) {
			if (hasFirstRowHeader.isSelected()) {
				selectedFile.setFirstRowAsHeader(true);
				columnName.setDisable(true);
				((TableColumn) datacolumns.getColumns().get(0)).setVisible(false);
			} else {
				selectedFile.setFirstRowAsHeader(false);
				columnName.setDisable(false);
				((TableColumn) datacolumns.getColumns().get(0)).setVisible(true);
			}

		}
	}

	/**
	 * Creates a dialog for the user to select an existing xml file.
	 * @param actionEvent JavaFX event
	 */
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

	/**
	 * Sets the metadata property for the datafile when the checkbox is selected.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void addMetaData(ActionEvent actionEvent) {
		if (addmetacheck.isSelected()) {
			metacolumnName.setDisable(false);
			metacolumntype.setDisable(false);
			metacolumnformat.setDisable(false);
			metacolumnvalue.setDisable(false);
		} else {
			metacolumnName.setDisable(true);
			metacolumntype.setDisable(true);
			metacolumnformat.setDisable(true);
			metacolumnvalue.setDisable(true);
		}
	}

	/**
	 * Sets the metadata values of the file.
	 */
	public void setMetaData() {
		logger.info(String.format(
				"Creating metadata: Columnname \"%s\" is a %s and the format is \"%s\": %s",
				metacolumnName.getText(), metacolumntype.getValue(),
				metacolumnformat.getText(), metacolumnvalue.getText()));

		try {
			if (addmetacheck.isSelected()) {
				selectedFile.createMetaDataValue(
						metacolumnvalue.getText(),
						new ColumnInfo(metacolumnName.getText(),
								DataFile.getColumnType((String) metacolumntype.getValue()),
								metacolumnformat.getText())
				);
			}
		} catch (NumberFormatException | DateTimeParseException e) {
			logger.log(Level.SEVERE, "Value: \"" + metacolumnvalue.getText()
					+ "\" can not be parsed to a " + metacolumntype.getValue().toString()
					+ ": " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "Value: \"" + metacolumnvalue.getText()
					+ "\" can not be parsed to a " + metacolumntype.getValue().toString()
					+ " using format \"" + metacolumnformat.getText() + "\": " + e.getMessage());
		}
	}

	/**
	 * Closes the dialog when the cancel button is pressed.
	 * @param actionEvent JavaFX event
	 */
	@FXML
	public void handleCancelButton(ActionEvent actionEvent) {
		dialog.close();
	}

	/**
	 * Applies the changes of the fields to the selected datafile.
	 * @param actionEvent JavaFx event
	 */
	@FXML
	public void applyChanges(ActionEvent actionEvent) {
		setMetaData();
		selectedFile.setStartLine(Integer.parseInt(startLine.getText()));
		selectedFile.setEndLine(Integer.parseInt(endLine.getText()));
		if (selectedFile instanceof PlainTextFile) {
			((PlainTextFile) selectedFile).setDelimiter(delimiter.getText());
		}
		setDataFilePath();
		apply.setDisable(true);
	}

	private void disableAll(boolean value) {
		datacolumns.setDisable(value);
		columnName.setDisable(value);
		columntype.setDisable(value);
		columnFormat.setDisable(value);
		hasFirstRowHeader.setDisable(value);
		startLine.setDisable(value);
		endLine.setDisable(value);
		delimiter.setDisable(value);
		addmetacheck.setDisable(value);
		metacolumnName.setDisable(value);
		metacolumntype.setDisable(value);
		metacolumnformat.setDisable(value);
		metacolumnvalue.setDisable(value);
		apply.setDisable(value);
	}
}
