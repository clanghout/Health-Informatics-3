package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import model.data.value.StringValue;

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
	
	private DataTable currentTable;
	private DataModel model;

	
	/**
	 * Creates a new TableViewController.
	 */
	public TableViewController() {
	}
	
	/**
	 * Loads the data from the model and updates the view for the user.
	 */
	private void fillTable() {
		currentTable = model.get(0);
		logger.info("update table: " + currentTable);
		tableView.getItems().clear();
		tableView.getColumns().clear();
		
		List<DataColumn> columns = currentTable.getColumns();
		Iterator<DataRow> rowIterator = currentTable.iterator();
		
		tableView.setPlaceholder(new Label("Loading..."));
		fillTableHeaders(columns);
		
		while (rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			for (int i = 0; i < columns.size(); i++) {
				StringValue value = (StringValue) currentRow.getValue(columns.get(i));
				String val = value.getValue();
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
			fxColumn.setPrefWidth(100);
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
	 * Sets the model that will be observed.
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
		model.addObserver(this);
	}
	
	/**
	 * If the model changes, this method is called. It is mainly checked if the observed 
	 * DataModel changes.
	 * @param o The observable object that changes
	 * @param arg The eventual parameter that is passed by the observable
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DataModel) {
			updateList();
			fillTable();
		}
	}

	private void updateList() {
		inputTables.setItems(model.getObservableList());
	}
}
