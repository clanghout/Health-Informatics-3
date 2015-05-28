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
	private ComboBox<DataTable> table;
	@FXML
	private ComboBox<DataColumn> input1;
	@FXML
	private ComboBox<DataColumn> input2;

	private DataModel model;
	private MainUIController mainUIController;

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
		input1.setDisable(true);
		input2.setDisable(true);
		visualization.setMaxWidth(Double.MAX_VALUE);
		table.setMaxWidth(Double.MAX_VALUE);
		input1.setMaxWidth(Double.MAX_VALUE);
		input2.setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 * Init method after a model is read.
	 *
	 * @param mainUIController the main controller.
	 */
	public void initializeVisualisation(MainUIController mainUIController) {
		this.mainUIController = mainUIController;
//		visualization.setDisable(false);
		table.setDisable(false);

		visualization.setItems(FXCollections.observableArrayList(
				"BarChart", "BoxPlot"));
		updateTableChoose();

	}

	/**
	 * Set the items of a combobox to the columns of the dataTable.
	 *
	 * @param inputBox  the comboBox that specifies the axis of the graph
	 * @param dataTable the datatable used for the graph
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
		table.valueProperty().addListener((observable, oldValue, newValue) -> {
			setColumnDropDown(input1, newValue);
			setColumnDropDown(input1, newValue);
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DataModel) {
			updateTableChoose();
		}
	}
}
