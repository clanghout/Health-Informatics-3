package controllers.wizard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;
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
import java.util.List;

/**
 * Controller class for the wizard to make the user able to create a datafile specifying xml.
 *
 * @author Paul.
 */
public class XmlWizardController {

	@FXML private ComboBox columntype;
	@FXML private TextField columnName;
	@FXML private TableView datacolumns;
	@FXML private ComboBox importAs;
	@FXML private TableView<ObservableList<StringProperty>> datafiles;
	@FXML private Parent root;
	@FXML private TextField fileselectfield;

	/**
	 * Initializes the controller by filling the static content of elements in the view.
	 */
	public void initialize() {
		datafiles.getColumns().add(createColumn(0, "Filename"));
		datafiles.getColumns().add(createColumn(1, "Type"));

		datacolumns.getColumns().add(createColumn(0, "Column name"));
		datacolumns.getColumns().add(createColumn(1, "Type"));

		importAs.setItems(
				FXCollections.observableArrayList(
						"xls", "xlsx", "plaintext"
				)
		);
		columntype.setItems(
				FXCollections.observableArrayList(
						"string", "int", "float", "datetime", "date", "time"
				)
		);
	}

	@FXML
	public void selectFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select Data File");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);

		File file = fileChooser.showOpenDialog(root.getScene().getWindow());
		fileselectfield.setText(file.getPath());
	}

	private TableColumn<ObservableList<StringProperty>, String> createColumn(int index,
																			 String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column
				= new TableColumn<>(columnTitle);
		column.setCellValueFactory(
				cellDataFeatures -> cellDataFeatures.getValue().get(index));
		return column;
	}

	@FXML
	public void handleImportButton(ActionEvent actionEvent) {
		ObservableList<StringProperty> row = FXCollections.observableArrayList();
		row.add(new SimpleStringProperty(fileselectfield.getText()));
		row.add(new SimpleStringProperty((String) importAs.getValue()));
		datafiles.getItems().add(row);
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
		for (ObservableList<StringProperty> val : datafiles.getItems()) {
			DataFile file = DataFile.createDataFile(val.get(0).getValue(), val.get(1).getValue());
			res.add(file);
		}
		return res;
	}

	@FXML
	public void createXml(ActionEvent actionEvent) {
		XmlWriter writer = new XmlWriter(createDataFiles());
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(writer.createDocument());
//			StreamResult result = new StreamResult(new File("/res/goodsaved.xml"));

			//Debug: print xml to console
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
