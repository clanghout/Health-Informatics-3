package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import model.data.*;
import view.SaveDialog;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controls the visualization of the table in the user interface.
 *
 * @author Paul
 */
public class TableViewController implements Observer {

	private static final String CODE_COLUMN_NAME = "Code";
	private Logger logger = Logger.getLogger("TabViewController");

	@FXML
	private TableView<ObservableList<StringProperty>> tableView;
	@FXML
	private ListView<TableWrapper> inputTables;
	@FXML
	private Button saveButton;
	@FXML
	private Label saveStatus;

	private DataModel model;
	private DataTable currentTable;



	/**
	 * Creates a new TableViewController.
	 */
	public TableViewController() {
	}

	/**
	 * Initializes the table by assigning a changelistener for the listview
	 * with the tables. If the selection changes, the table will update.
	 */
	public void initialize() {
		saveButton.setDisable(true);
		saveStatus.setTextFill(Color.RED);
		logger.info("initializing listview changelistener");
		ChangeListener<TableWrapper> listener = (ov, oldValue, newValue) -> {
			if (!(newValue == null)) {
				logger.info("changing content of tableView with content of "
						+ newValue.toString());
				currentTable = newValue.getTable();
				fillTable(newValue.getTable());
			}
		};
		this.inputTables.getSelectionModel().selectedItemProperty().addListener(listener);
	}

	/**
	 * Loads the data from the model and updates the view for the user.
	 */
	private void fillTable(DataTable table) {
		logger.info("update table: " + table);
		tableView.getItems().clear();
		tableView.getColumns().clear();

		List<DataColumn> columns = table.getColumns();
		Iterator<DataRow> rowIterator = table.iterator();

		fillTableHeaders(columns);

		while (rowIterator.hasNext()) {
			StringBuilder codesBuilder = new StringBuilder();
			Row currentRow = rowIterator.next();
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			if (!currentRow.getCodes().isEmpty()) {
				Set<String> codes = currentRow.getCodes();
				logger.info("Row has codes " + codes);
				for (String code : codes) {
					codesBuilder.append(code).append("\n");
				}
			}
			for (DataColumn column : columns) {
				String val = currentRow.getValue(column).toString();
				row.add(new SimpleStringProperty(val));
			}
			row.add(new SimpleStringProperty(codesBuilder.toString()));
			tableView.getItems().add(row);
		}
	}

	/**
	 * Fills the headers of the table.
	 *
	 * @param columns A List containing the DataColumns
	 */
	private void fillTableHeaders(List<DataColumn> columns) {
		int i = 0;
		while (i < columns.size()) {
			TableColumn<ObservableList<StringProperty>, String> fxColumn
					= createColumn(i, columns.get(i).getName());
			fxColumn.getStyleClass().add("table-column");
			tableView.getColumns().add(fxColumn);
			i++;
		}
		addCodesColumn(i);
	}

	/**
	 * Adds a column for the codes into the tableview.
	 */
	private void addCodesColumn(int index) {
		TableColumn<ObservableList<StringProperty>, String> fxColumn
				= new TableColumn<>(CODE_COLUMN_NAME);
		fxColumn.setCellValueFactory(code -> code.getValue().get(index));
		tableView.getColumns().add(fxColumn);
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
	 * Sets the model that will be observed and initializes the first view of the model.
	 *
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
		saveButton.setDisable(false);
		currentTable = model.get(0);
		model.addObserver(this);
		updateList();
		fillTable(currentTable);
	}

	@FXML
	protected void handleSaveAction() {
		saveStatus.setText("");
		try {
			SaveDialog saveDialog = new SaveDialog(model);
			saveDialog.show();
		} catch (IOException e) {
			saveStatus.setText("An error occurred, data not saved!");
		}
	}

	/**
	 * If the model changes, this method is called. It is mainly checked if the observed
	 * DataModel changes. If this happens, the view of the table will be updated.
	 *
	 * @param o   The observable object that changes
	 * @param arg The eventual parameter that is passed by the observable
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DataModel) {
			logger.info("Model changed");
			updateList();
			fillTable(currentTable);
		}
		saveStatus.setText("");
	}

	private void updateList() {

		List<DataTable> tables = model.getTables();
		ArrayList<TableWrapper> wrappers = new ArrayList<>(tables
				.stream()
				.map(TableWrapper::new)
				.collect(Collectors.toList()));



		inputTables.setItems(FXCollections.observableArrayList(wrappers));
	}

	/**
	 * A wrapper for the table to be used in the ListView, since using the tables directly
	 * incurs massive performance issues.
	 */
	private static final class TableWrapper {
		private DataTable table;

		private TableWrapper(DataTable table) {
			this.table = table;
		}

		private DataTable getTable() {
			return table;
		}

		@Override
		public String toString() {
			return table.getName();
		}
	}
}
