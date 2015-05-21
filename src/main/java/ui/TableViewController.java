package ui;

import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Logger;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;
import model.data.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controls the visualization of the table in the user interface.
 * @author Paul
 *
 */
public class TableViewController implements Observer {
	
	private Logger logger = Logger.getLogger("TabViewController");
	
	@FXML private TableView tableView;
	
	private MainUIController mainUIController;
	
	private DataModel model;
	private ObservableList<Row> data = FXCollections.observableArrayList();
	
	/**
	 * Creates a new TableViewController.
	 */
	public TableViewController() {
	}
	
	public void initialize(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
		tableView.setItems(data);
	}
	
	public void fillTable()  {
		logger.info("update table");
		for (DataTable table: model) {
			Map<String, DataColumn> columns = table.getColumns();
			Set<String> nameSet = columns.keySet();
			Iterator<DataRow> rowIterator = table.iterator();
			for(String name : nameSet) {
				TableColumn uiColumn = new TableColumn(name);
				tableView.getColumns().add(uiColumn);
			}
			while(rowIterator.hasNext()) {
				DataRow row = rowIterator.next();
				data.add(row);
			}
		}
		for(int i = 0; i< data.size(); i++) {
			System.out.println(data.get(i));
		}
	}
	
	/**
	 * Sets the model that will be observed.
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
		model.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DataModel) {
			fillTable();
		}
	}
}
