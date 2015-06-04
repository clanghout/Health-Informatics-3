package controllers.visualizations;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.NumberValue;
import model.process.functions.Maximum;
import model.process.functions.Minimum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for BarChart option in the visualization.
 * Fills the input VBOX in the FXML file.
 * Creates BarChart with selected columns in the input.
 * <p>
 * Created by Chris on 28-5-2015.
 */
public class BarChartController extends ChartController {
	private DataTable table;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	private VBox vBox;
	private DataColumn xCol;
	private DataColumn yCol;
	private boolean xSet = false;
	private boolean ySet = false;
	public static final int YAXIS_SEPARATION = 5;

	private ComboBox<DataColumn> xAxisBox;
	private ComboBox<DataColumn> yAxisBox;
	private Label xAxisErrorLabel;
	private Label yAxisErrorLabel;
	private ChangeListener<DataColumn> dataColumnChangeListener;


	/**
	 * Create new BarChartController object.
	 *
	 * @param table the table for the graph.
	 * @param vBox  the box for the input comboBoxes.
	 */
	public BarChartController(DataTable table, VBox vBox) {
		this.table = table;
		this.vBox = vBox;
	}

	/**
	 * Initialize the fxml objects for the barChart.
	 */
	public void initializeFields() {
		xAxisBox = new ComboBox<>();
		xAxisErrorLabel = new Label();
		xAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		yAxisBox = new ComboBox<>();
		yAxisErrorLabel = new Label();
		yAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		setColumnDropDown(xAxisBox, table);
		setColumnDropDown(yAxisBox, table);
	}

	/**
	 * Sets the Listener for the xAxis ComboBox.
	 * When the value changes, the data of the selected column will be added to the
	 * CategoryAxis of the barChart. An error message is shown to the user when the column
	 * does not contain appropriate values for a CategoryAxis.
	 */
	public void setXAxisEventListener() {
		xAxisBox.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
			xCol = newValue1;
			List<String> dataxcol = table.getRows().stream()
					.map(row -> row.getValue(xCol).toString())
					.collect(Collectors.toList());
			try {
				xAxis = new CategoryAxis(FXCollections.observableArrayList(dataxcol));
				setErrorLabel(xAxisErrorLabel, "");
				xSet = true;
			} catch (Exception e) {
				setErrorLabel(xAxisErrorLabel, "Please select a column with distinct values.");
			}
		});
	}

	/**
	 * Sets the Listener for the yAxis ComboBox.
	 * The NumberAxis is created and the scale is set.
	 */
	public void setYAxisEventListener() {
		dataColumnChangeListener = (observable, oldValue, newValue) -> {
			yCol = newValue;
			DataDescriber<NumberValue> yColDescriber = new RowValueDescriber<>(yCol);
			try {
				float max = new Maximum(table, yColDescriber).calculate().getValue();
				float min = new Minimum(table, yColDescriber).calculate().getValue() - 1;
				int sep = computeSeparatorValue(max, min);
				yAxis = new NumberAxis(yCol.getName(), min, max, sep);
				setErrorLabel(yAxisErrorLabel, "");
				ySet = true;
			} catch (Exception e) {
				setErrorLabel(yAxisErrorLabel, "Please select a column with number values.");
			}
		};
		yAxisBox.valueProperty().addListener(dataColumnChangeListener);
	}

	/**
	 * Called to initialize the barChartController object.
	 */
	public void initialize() {
		initializeFields();
		setXAxisEventListener();
		setYAxisEventListener();
		vBox.getChildren().addAll(xAxisErrorLabel, xAxisBox, yAxisErrorLabel, yAxisBox);
	}

	/**
	 * Compute approximately a tenth of the range of the axis.
	 *
	 * @param max the maximum value of the axis.
	 * @param min the minimum value of the axis.
	 * @return the computed seperator value as int.
	 */
	public int computeSeparatorValue(float max, float min) {
		return (int) (max - min) / YAXIS_SEPARATION;
	}

	/**
	 * Set the message to an error label.
	 * @param label the label wich will show the error.
	 * @param message the message for in the label.
	 */
	public void setErrorLabel(Label label, String message) {
		label.setTextFill(Color.RED);
		label.setText(message);
	}

	/**
	 * Check if both axes are set.
	 *
	 * @return true if both xAxis and yAxis have been initialized.
	 */
	public boolean axesSet() {
		return xSet && ySet;
	}

	/**
	 * Create the BarChart that can be added to the view.
	 *
	 * @return BarChart object.
	 */
	public BarChart create() {
		BarChart res = new BarChart<>(xAxis, yAxis);
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName(yCol.getName());
		for (DataRow row : table.getRows()) {
			series1.getData().add(new XYChart.Data(row.getValue(xCol).toString(),
					Integer.valueOf(row.getValue(yCol).getValue().toString())));
		}
		res.getData().add(series1);
		return res;
	}
}