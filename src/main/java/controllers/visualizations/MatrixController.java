package controllers.visualizations;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for State Transition Matrix.
 * Created by Chris on 9-6-2015.
 */
public class MatrixController {
	private DataModel model;
	private Set<String> codes;
	private Logger logger = Logger.getLogger("MatrixController");

	/**
	 * Construct new MatrixController.
	 * @param model DataModel of the application
	 */
	public MatrixController(DataModel model) {
		this.model = model;
	}

	/**
	 * Collect the codes from all the rows in the complete DataModel and add them to the codes set.
	 */
	private void collectCodes() {
		codes = new HashSet<>();
		logger.log(Level.INFO, "Tables in model = " + model.getObservableList());
		for (DataTable table: model.getObservableList()) {
			logger.log(Level.INFO, "checking table " + table);
			for (DataRow row : table.getRows()) {
					codes.addAll(row.getCodes());
			}
		}
	}

	/**
	 * Check if data exists and then call to collectCodes.
	 */
	public void initialize() {
		if (model.size() > 0) {
			DataTable table = model.get(0);
			if (table.getRowCount() > 0) {
				collectCodes();
			}
		}
	}

	/**
	 * Returns the set of codes of the model.
	 * @return Set of codes.
	 */
	public Set<String> getCodes() {
		initialize();
		return codes;
	}


	/**
	 * Create a TableView object.
	 * @param codes the list of codes for the right axis.
	 * @return createImage of the tableView
	 */
	public TableView create(List<String> codes) {
		System.out.println("call create");
		TableView<ObservableList<StringProperty>> matrix = new TableView<>();
		System.out.println("create matrix = " + matrix + " now call fill");
		fillTableHeaders(matrix, codes);
		for (String code : codes) {
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			fillTableRow(row, code);
			matrix.getItems().add(row);
		}
		return matrix;
//		return createImage(matrix);
	}

	private void fillTableRow(ObservableList<StringProperty> row, String code) {
		row.add(new SimpleStringProperty(code));


	}

	/**
	 * Fills the headers of the table.
	 *
	 * @param columns A List containing the DataColumns
	 */
	private void fillTableHeaders(TableView matrix, List<String> columns) {
		System.out.println("fill headers callede");
		matrix.getColumns().add(createColumn(0, ""));
		System.out.println("add column");
		for (int i = 0; i < columns.size(); i++) {
			matrix.getColumns().add(createColumn(i + 1, columns.get(i)));
		}
	}

	/**
	 * Creates a new TableColumn based on an observable list with StringProperties.
	 *
	 * @param index       The index of the column that will be created
	 * @param columnTitle The name of the column
	 * @return The created TableColumn
	 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html">
	 * The TableView Class</a>
	 */
	private TableColumn<ObservableList<StringProperty>, String> createColumn(int index,
	                                                                         String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column
				= new TableColumn<>(columnTitle);
		column.setCellValueFactory(
				cellDataFeatures -> cellDataFeatures.getValue().get(index));
		return column;
	}

	/**
	 * Create an image of a TableView containing the matrix.
	 * @return image of the table.
	 */
	public WritableImage createImage(TableView view) {

		return null;
	}
}
