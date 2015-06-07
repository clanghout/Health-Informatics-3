package controllers.visualizations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import model.data.DataColumn;
import model.data.DataTable;

/**
 * Abstract ChartController specifying the controller for a javaFX chart.
 * Created by Chris on 4-6-2015.
 */
public abstract class ChartController {
	protected DataTable table;
	protected CategoryAxis xAxis;
	protected NumberAxis yAxis;
	protected VBox vBox;
	protected DataColumn xCol;
	protected DataColumn yCol;
	protected boolean xSet = false;
	protected boolean ySet = false;
	protected ComboBox<DataColumn> xAxisBox;
	protected ComboBox<DataColumn> yAxisBox;
	protected Label xAxisErrorLabel;
	protected Label yAxisErrorLabel;

	public ChartController(DataTable table, VBox vBox) {
		this.table = table;
		this.vBox = vBox;
	}

	/**
	 * Creation of the controller.
	 */
	public abstract void initialize();

	/**
	 * Checks if all data Axes are set and contain valid information.
	 * Used to check before building the actual graph.
	 * @return true if all axes are well defined.
	 */
	public boolean axesSet() {
		return xSet && ySet;
	}

	/**
	 * Create the actual javaFX element
	 * @return JavaFX chart with valid data.
	 */
	public abstract javafx.scene.chart.Chart create();

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
	 * Set the message to an error label.
	 *
	 * @param label   the label wich will show the error.
	 * @param message the message for in the label.
	 */
	public void setErrorLabel(Label label, String message) {
		label.setTextFill(Color.RED);
		label.setText(message);
	}

	/**
	 * Initialize the fxml objects for ChartController.
	 */
	public void initializeFields() {
		xAxisBox = new ComboBox<>();
		xAxisBox.setPromptText("x-Axis");
		xAxisErrorLabel = new Label();
		xAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		yAxisBox = new ComboBox<>();
		yAxisBox.setPromptText("y-Axis");
		yAxisErrorLabel = new Label();
		yAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		setColumnDropDown(xAxisBox, table);
		setColumnDropDown(yAxisBox, table);
	}


}
