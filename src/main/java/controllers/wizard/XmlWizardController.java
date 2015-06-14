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
import model.input.reader.XmlReader;
import model.output.XmlWriter;
import org.xml.sax.SAXException;
import view.Dialog;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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
	@FXML private ComboBox metacolumntype;
	@FXML private TextField metacolumnName;
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
			"string", "int", "float", "datetime", "date", "time"
	);

	private final ChangeListener<DataFile> listener = (ov, oldValue, newValue) -> {
		if (!(newValue == null)) {
			selectedFile = newValue;
			disableAll(false);
			fillElements();
		} else {
			disableAll(true);
		}
	};

	private final ChangeListener<String> startLineChangeListener = (ov, oldValue, newValue) -> {
		if (newValue != null && selectedFile != null) {
			if (!(newValue.equals(String.valueOf(selectedFile.getStartLine())))) {
				apply.setDisable(false);
			} else {
				apply.setDisable(true);
			}
		}
	};

	private final ChangeListener<String> endLineChangeListener = (ov, oldValue, newValue) -> {
		if (newValue != null && selectedFile != null) {
			if (!(newValue.equals(String.valueOf(selectedFile.getEndLine())))) {
				apply.setDisable(false);
			} else {
				apply.setDisable(true);
			}
		}
	};

	private final ChangeListener<String> metacolumnNameListener = (ov, oldValue, newValue) -> {
		if (newValue != null
				&& selectedFile != null
				&& !(newValue.equals(selectedFile.getMetaDataColumnName()))) {

			apply.setDisable(false);
		} else {
			apply.setDisable(true);
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
			}
			else {
				columnFormat.setText("");
				columnFormat.setDisable(true);
			}
		}
	});
	private ListChangeListener datacolumnsListener = new ListChangeListener() {
		boolean suspended;

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
		metacolumntype.getSelectionModel().select("string");
		columntype.valueProperty().addListener(columnTypeListener);
		datacolumns.getColumns().addListener(datacolumnsListener);
		disableAll(true);
		metacolumnName.textProperty().addListener(metacolumnNameListener);
		metacolumntype.valueProperty().addListener(metacolumntypeListener);
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
			String type = getTypeByExtension(
					fileChooser.getSelectedExtensionFilter().getExtensions().get(0));
			addDataFile(file.getPath(), type);
			fileselectfield.setText(file.getPath());
		}
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
			apply.setDisable(true);
		}
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
			selectedFile.setHasMetaData(true);
			metacolumnName.setDisable(false);
			metacolumntype.setDisable(false);
		} else {
			selectedFile.setHasMetaData(false);
			metacolumnName.setDisable(true);
			metacolumntype.setDisable(true);
		}
	}

	/**
	 * Sets the metadata values of the file.
	 */
	public void setMetaData() {
		selectedFile.setMetaDataColumnName(metacolumnName.getText());
		selectedFile.setMetaDataType(DataFile.getColumnType(
				(String) metacolumntype.getSelectionModel().getSelectedItem()));
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
		addmetacheck.setDisable(value);
		metacolumnName.setDisable(value);
		metacolumntype.setDisable(value);
		apply.setDisable(value);
	}
}
