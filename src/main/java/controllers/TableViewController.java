package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;
import model.data.Row;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Controls the visualization of the table in the user interface.
 * @author Paul
 *
 */
public class TableViewController implements Observer {
	
	private Logger logger = Logger.getLogger("TabViewController");
	
	@FXML 
	private TableView<ObservableList<StringProperty>> tableView;
	@FXML
	private ListView<DataTable> inputTables;
	
	private DataModel model;
	private DataTable currentTable;

	
	/**
	 * Creates a new TableViewController.
	 */
	public TableViewController() {
	}
	
	public void initialize() {
		logger.info("initializing listview changelistener");
		ChangeListener<DataTable> listener = new ChangeListener<DataTable>() {
            public void changed(ObservableValue<? extends DataTable> ov, 
            	DataTable oldValue, DataTable newValue) {
            	logger.info("changing content of tableView with content of " 
            				+ newValue.getName());
	            currentTable = newValue;
            	fillTable(newValue);
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
		
		tableView.setPlaceholder(new Label("Loading..."));
		fillTableHeaders(columns);
		
		while (rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			for (int i = 0; i < columns.size(); i++) {
				String val = currentRow.getValue(columns.get(i)).toString();
				row.add(new SimpleStringProperty(val));
			}
			tableView.getItems().add(row);
		}
	}
	
	/**
	 * Fills the headers of the table.
	 * @param columns A List containing the DataColumns
	 */
	private void fillTableHeaders(List<DataColumn> columns) {
		for (int i = 0; i < columns.size(); i++) {
			TableColumn<ObservableList<StringProperty>, String> fxColumn 
				= createColumn(i, columns.get(i).getName());
			fxColumn.getStyleClass().add("table-column");
			tableView.getColumns().add(fxColumn);
		}
	}
	
	/**
	 * Creates a new TableColumn based on an observable list with StringProperties.
	 * @param index The index of the column that will be created
	 * @param columnTitle The name of the column
	 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html">
	 * 	The TableView Class</a>
	 * @return The created TableColumn
	 */
	private TableColumn<ObservableList<StringProperty>, String> createColumn(int index, 
																			String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column 
									= new TableColumn<>(columnTitle);
		column.setCellValueFactory(
				new Callback<CellDataFeatures<ObservableList<StringProperty>, String>, 
												ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
				return cellDataFeatures.getValue().get(index);
			}
		});
		return column;
	}
	
	/**
	 * Sets the model that will be observed and initializes the first view of the model.
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
		currentTable = model.get(0);
		model.addObserver(this);
		updateList();
		fillTable(currentTable);
	}
	
	/**
	 * If the model changes, this method is called. It is mainly checked if the observed 
	 * DataModel changes. If this happens, the view of the table will be updated.
	 * @param o The observable object that changes
	 * @param arg The eventual parameter that is passed by the observable
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DataModel) {
			updateList();
			fillTable(currentTable);
		}
	}
	private void updateList() {

		inputTables.setItems(model.getObservableList());
	}
}
