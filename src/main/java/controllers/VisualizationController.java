package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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
	private ComboBox<model.data.DataTable> table;
	@FXML
	private ComboBox<DataColumn> xAxis;
	@FXML
	private ComboBox<DataColumn> yAxis;

	private DataModel model;
	private MainUIController mainUIController;

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

	/**
	 * initialization module for the visualisation controller.
	 * The dropdown menu's are disabled while no DataModel is loaded.
	 */
	public void initialize() {
		visualization.setDisable(true);
		table.setDisable(true);
		xAxis.setDisable(true);
		yAxis.setDisable(true);
		visualization.setMaxWidth(Double.MAX_VALUE);
		table.setMaxWidth(Double.MAX_VALUE);
		xAxis.setMaxWidth(Double.MAX_VALUE);
		yAxis.setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 * Init method after a model is read.
	 * @param mainUIController the main controller.
	 */
	public void initializeVisualisation(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
		visualization.setDisable(false);
		table.setDisable(false);

		System.out.println("visualization = " + visualization);
		visualization.setItems(FXCollections.observableArrayList(
				"BarChart", "BoxPlot"));
		System.out.println("table = " + table);
		System.out.println("model = " + model);
		updateTableChoose();
		table.valueProperty().addListener((observable, oldValue, newValue) -> {
			setColumnDropDown(xAxis, newValue);
			setColumnDropDown(yAxis, newValue);
		});
	}

	/**
	 * Set the items of a combobox to the columns of the dataTable.
	 * @param axis the comboBox that specifies the axis of the graph
	 * @param dataTable the datatable used for the graph
	 */
	public void setColumnDropDown(ComboBox<DataColumn> axis, DataTable dataTable) {
		axis.setDisable(false);
		ObservableList<DataColumn> columns =
				FXCollections.observableArrayList(dataTable.getColumns());
		axis.setItems(columns);
		axis.setConverter(new StringConverter<DataColumn>() {
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

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof DataModel) {
			updateTableChoose();
		}
	}
}
