package controllers.wizard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;
import model.data.DataTable;
import model.exceptions.DataFileNotRecognizedException;
import model.input.file.DataFile;
import model.output.XmlWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controller class for the wizard to make the user able to create a datafile specifying xml.
 *
 * @author Paul.
 */
public class XmlWizardController {

	@FXML private ComboBox metacolumntype;
	@FXML private TextField metacolumnName;
	@FXML private ComboBox columntype;
	@FXML private TextField columnName;
	@FXML private TableView datacolumns;
	@FXML private ListView<DataFile> datafiles;
	@FXML private Parent root;
	@FXML private TextField fileselectfield;

	private final ObservableList typesSelect = FXCollections.observableArrayList(
			"string", "int", "float", "datetime", "date", "time"
	);
	private HashMap<DataFile, ObservableList<StringProperty>> fileColumns = new HashMap<>();
	private DataFile selectedFile;

	/**
	 * Initializes the controller by filling the static content of elements in the view.
	 */
	public void initialize() {
		ChangeListener<DataFile> listener = (ov, oldValue, newValue) -> {
			if (!(newValue == null)) {
				selectedFile = newValue;
				datacolumns.setItems(fileColumns.get(newValue));
			}
		};
		this.datafiles.getSelectionModel().selectedItemProperty().addListener(listener);
		datacolumns.getColumns().add(createColumn(0, "Column name"));
		datacolumns.getColumns().add(createColumn(1, "Type"));

		metacolumntype.setItems(typesSelect);
		columntype.setItems(typesSelect);
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
		fileColumns.put(file,FXCollections.observableArrayList());
	}

	@FXML
	public void addColumnRow(ActionEvent actionEvent) {
		ObservableList<StringProperty> row = FXCollections.observableArrayList();
		row.add(new SimpleStringProperty(columnName.getText()));
		row.add(new SimpleStringProperty((String) columntype.getValue()));
		datacolumns.getItems().add(row);
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
	}

	private void writeXmlToFile(File file) {
		XmlWriter writer = new XmlWriter(createDataFiles());
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(writer.createDocument());

//			StreamResult result = new StreamResult(file);
			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);

		} catch (ParserConfigurationException
				| FileNotFoundException
				| ClassNotFoundException
				| TransformerException e) {
			e.printStackTrace();
		}
	}
}
