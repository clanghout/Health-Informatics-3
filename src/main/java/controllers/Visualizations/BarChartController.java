package controllers.Visualizations;

import controllers.VisualizationController;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
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
public class BarChartController extends VisualizationController {
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
	 * @param table the table for the graph.
	 * @param vBox the box for the input comboBoxes.
	 */
	public BarChartController(DataTable table, VBox vBox) {
		this.table = table;
		this.vBox = vBox;
	}

	public void initialize() {
		ComboBox<DataColumn> xAxisBox = new ComboBox<>();
		ComboBox<DataColumn> yAxisBox = new ComboBox<>();
		setColumnDropDown(xAxisBox, table);
		setColumnDropDown(yAxisBox, table);
		xAxisBox.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
			xSet = true;
			xCol = newValue1;
			List<String> dataxcol = table.getRows().stream()
					.map(row -> row.getValue(xCol).toString())
					.collect(Collectors.toList());
			xAxis = new CategoryAxis(FXCollections.observableArrayList(dataxcol));
		});
		yAxisBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			ySet = true;
			yCol = newValue;
			DataDescriber<NumberValue> yColDescriber = new RowValueDescriber<>(yCol);
//			Double max = (double) new Maximum(table, yColDescriber).calculate().getValue();
//			Double min = (double) new Minimum(table, yColDescriber).calculate().getValue();
			// sep is the nearest power of 10 of max devided by YAXIS_SEPERATOR.
			// This creates a value that seperates the y-axis in approximately 10 values.
//			int sep = (int) Math.pow(BASE,
//					Math.round(Math.log10(max / YAXIS_SEPARATION) - Math.log10(MEAN) + ROUND));
			yAxis = new NumberAxis(yCol.getName(), 100, 200, 10);
		});
		vBox.getChildren().addAll(xAxisBox, yAxisBox);
	}

	/**
	 * Check if both axes are set.
	 * @return true if both xAxis and yAxis have been initialized.
	 */
	public boolean axesSet() {
		return xSet && ySet;
	}

	/**
	 * Create the BarChart that can be added to the view.
	 * @return BarChart object.
	 */
	public BarChart create() {

		BarChart res = new BarChart(xAxis, yAxis);

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