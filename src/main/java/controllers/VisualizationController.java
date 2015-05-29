package controllers;

import controllers.Visualizations.BarChartController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;

import java.util.Observable;
import java.util.Observer;

/**
 * Control visualisation.
 * Created by Chris on 26-5-2015.
 */
public class VisualizationController implements Observer {
	@FXML
	private ComboBox<String> visualization;
	@FXML
	private ComboBox<DataTable> table;
	@FXML
	private VBox visualizationGraph;
	@FXML
	private VBox visualizationInput;

	private DataModel model;
	private DataTable dataTable;
	private BarChartController bcc;

	/**
	 * Constructor for Visualization controller.
	 */
	public VisualizationController() {
	}

	/**
	 * Sets the model that will be observed.
	 *
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
		model.addObserver(this);

	}

	/**
	 * initialization module for the visualisation controller.
	 * The dropdown menu's are disabled while no DataModel is loaded.
	 */
	public void initialize() {
		visualization.setDisable(true);
		table.setDisable(true);
		visualization.setMaxWidth(Double.MAX_VALUE);
		table.setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 * Init method after a model is read.
	 */
	public void initializeVisualisation() {
		visualization.setDisable(false);
		table.setDisable(false);

		visualization.setItems(FXCollections.observableArrayList(
				"BarChart", "BoxPlot"));

		updateTableChoose();
		table.valueProperty().addListener((observable, oldValue, newValue) -> {
			this.dataTable = newValue;
			visualization.setDisable(false);
		});
		visualization.valueProperty().addListener((observable, oldValue, newValue) -> {
			switch (newValue) {
				case "BarChart":
					bcc = new BarChartController(dataTable, visualizationInput);
					bcc.initialize();
					break;
				default:
					break;
			}
		});
	}

	/**
	 * Set the items of a comboBox to the columns of the dataTable.
	 *
	 * @param inputBox  the comboBox that specifies the axis of the graph
	 * @param dataTable the dataTable used for the graph
	 */
	public void setColumnDropDown(ComboBox<DataColumn> inputBox, DataTable dataTable) {
		inputBox.setDisable(false);
		ObservableList<DataColumn> columns =
				FXCollections.observableArrayList(dataTable.getColumns());
		inputBox.setItems(columns);
		inputBox.setConverter(new StringConverter<DataColumn>() {
			@Override
			public String toString(DataColumn object) {
				return object.getName();
			}

			@Override
			public DataColumn fromString(String string) {
				return dataTable.getColumn(string);
			}
		});
	}

	/**
	 * set items of table to all the tables in the model.
	 */
	private void updateTableChoose() {
		table.setItems(model.getObservableList());

	}

	@FXML
	protected void handleGraphCreateButtonAction(ActionEvent event) {
		if(bcc.axesSet()) {
			visualizationGraph.getChildren().add(bcc.create());
		}
	}

	/**
	 * Update the instances in the table selection comboBox.
	 * Whenever the observer notifies, the update method is called.
	 *
	 * @param o   Observable dataModel where the tables in it can change on update.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 *            method.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DataModel) {
			updateTableChoose();
		}
	}
}
