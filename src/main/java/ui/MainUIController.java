package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.data.DataModel;

import java.util.logging.Logger;

/**
 * Controls the elements of the GUI.
 * @author Paul
 *
 */
public class MainUIController {

	@FXML private TableViewController tableViewController;
	@FXML private DataController dataController;
	@FXML private AnalysisController analysisController;
//	@FXML private VisualizationController visualizationController;
	
	private DataModel model;
	
	private Logger logger = Logger.getLogger("MainUIController");

	@FXML public void initialize() {
		this.tableViewController.initialize(this);
		this.dataController.initialize(this);
//		this.visualizationController.initialize(this);
	}
	
	/**
	 * Called when the "Quit" menubutton is pressed.
	 * @param event the event
	 */
	@FXML protected void handleQuitAction(ActionEvent event) {
		System.out.println(tableViewController);
		logger.info("Shutting down");
		System.exit(0);
	}
	
	public void setModel(DataModel model) {
		this.model = model;
		tableViewController.setDataModel(model);
		analysisController.setDataModel(model);
	}

}
