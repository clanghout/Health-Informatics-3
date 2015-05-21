package ui;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
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
	
	/**
	 * Creates a new TableViewController.
	 */
	public TableViewController() { 
		System.out.println(this);
//	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tab_view.fxml"));
//	    fxmlLoader.setController(this);
//	    try {
//	        fxmlLoader.load();
//	    } catch (Exception e) {
//			logger.log(Level.SEVERE, "Failed to read fxml file", e);
//	    }
	}
	
	public void initialize(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
	}
	
	public void fillTable()  {
		logger.info("update table");
		logger.info("Model = " + model);
			for (DataTable t: model) {
			Map<String, DataColumn> m = t.getColumns();
			Set<String> set = m.keySet();
			for (String header : set) {
				logger.info(header);
				TableColumn col = new TableColumn(header);
				tableView.getColumns().addAll(col);
			}
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
