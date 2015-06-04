package controllers.visualizations;

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
	public static final int YAXIS_SEPARATION = 10;
	public static final int BASE = 10;
	public static final double MEAN = 5.5;
	public static final double ROUND = 0.5;


	/**
	 * Create new BarChartController object.
	 *
	 * @param table the table for the graph.
	 * @param vBox  the box for the input comboBoxes.
	 */
	public BarChartController (DataTable table, VBox vBox) {
		this.table = table;
		this.vBox = vBox;
	}

	public void initialize () {
		ComboBox<DataColumn> xAxisBox = new ComboBox<>();
		Label xAxisErrorLabel = new Label();
		xAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		ComboBox<DataColumn> yAxisBox = new ComboBox<>();
		Label yAxisErrorLabel = new Label();
		yAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		setColumnDropDown(xAxisBox, table);
		setColumnDropDown(yAxisBox, table);
		xAxisBox.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
			xSet = true;
			xCol = newValue1;
			List<String> dataxcol = table.getRows().stream()
					.map(row -> row.getValue(xCol).toString())
					.collect(Collectors.toList());
			try {
				xAxis = new CategoryAxis(FXCollections.observableArrayList(dataxcol));
				setErrorLabel(xAxisErrorLabel, "");
			} catch (Exception e) {
				setErrorLabel(xAxisErrorLabel, "Please select a column with distinct values.");
			}
		});
		yAxisBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			ySet = true;
			yCol = newValue;
			DataDescriber<NumberValue> yColDescriber = new RowValueDescriber<>(yCol);
			try {
				float max = (float) new Maximum(table, yColDescriber).calculate().getValue();
				float min = (float) new Minimum(table, yColDescriber).calculate().getValue();
				int sep = computeSeperatorValue(max);
				yAxis = new NumberAxis(yCol.getName(), min, max, sep);
				setErrorLabel(yAxisErrorLabel, "");
			} catch (Exception e) {
				setErrorLabel(yAxisErrorLabel, "Please select a column with number values.");
			}
		});
		vBox.getChildren().addAll(xAxisErrorLabel, xAxisBox, yAxisErrorLabel, yAxisBox);
	}

	/**
	 * Compute aproximately a tenth of the maxvalue rouded to the nearest power of 10.
	 *
	 * @param max the maximum value of the axis.
	 * @return the computed seperator value as int.
	 */
	public int computeSeperatorValue (float max) {
		return (int) Math.pow(BASE,
				Math.round(Math.log10(max / YAXIS_SEPARATION) - Math.log10(MEAN) + ROUND));
	}

	public void setErrorLabel (Label label, String message) {
		label.setTextFill(Color.RED);
		label.setText(message);
	}

	/**
	 * The nearest power of 10 to the (max value devided by YAXIS_SEPERATOR).
	 * This value is used for splitting the y-axis into approximately 10.
	 *
	 * @param max
	 * @return the nearest power of 10 of max devided by YAXIS_SEPERATOR.
	 */
	public int setScale (float max) {
		return (int) Math.pow(BASE,
				Math.round(Math.log10(max / YAXIS_SEPARATION) - Math.log10(MEAN) + ROUND));

	}

	/**
	 * Check if both axes are set.
	 *
	 * @return true if both xAxis and yAxis have been initialized.
	 */
	public boolean axesSet () {
		return xSet && ySet;
	}

	/**
	 * Create the BarChart that can be added to the view.
	 *
	 * @return BarChart object.
	 */
	public BarChart create () {

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