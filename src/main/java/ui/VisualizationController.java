package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import model.data.DataModel;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Control visualisation.
 * Created by Chris on 26-5-2015.
 */
public class VisualizationController implements Observer {
	@FXML
	private ComboBox<String> visualisation;
	@FXML
	private ComboBox<model.data.DataTable> table;
	@FXML
	private ComboBox xAxis;
	@FXML
	private ComboBox yAxis;

	private DataModel model;
	private MainUIController mainUIController;
	private Logger logger = Logger.getLogger("VisualizationController");

	public VisualizationController() {
	}

	/**
	 * Sets the model that will be observed.
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
		model.addObserver(this);

	}

	public void initialize() {
		visualisation.setDisable(true);
		table.setDisable(true);
		xAxis.setDisable(true);
		yAxis.setDisable(true);
		visualisation.setMaxWidth(Double.MAX_VALUE);
		table.setMaxWidth(Double.MAX_VALUE);
		xAxis.setMaxWidth(Double.MAX_VALUE);
		yAxis.setMaxWidth(Double.MAX_VALUE);
	}

	public void initializeVisualisation(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
		visualisation.setDisable(false);
		table.setDisable(false);
//		xAxis.setDisable(false);
//		yAxis.setDisable(false);
		System.out.println("visualisation = " + visualisation);
		visualisation.setItems(FXCollections.observableArrayList(
				"Frequency graph", "BoxPlot"));

		System.out.println("table = " + table);
		System.out.println("model = " + model);
		updateTableChoose();
	}

	private void updateTableChoose() {
		table.setItems(model.getObservableList());
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof DataModel) {
			updateTableChoose();
		}
	}
}
