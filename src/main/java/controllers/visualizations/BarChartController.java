package controllers.visualizations;

import javafx.collections.FXCollections;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.process.describer.DataDescriber;
import model.process.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.NumberValue;
import model.exceptions.InputMismatchException;
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
	private VBox vBox;

	private DataColumn xCol;
	private DataColumn yCol;
	private ComboBox<ColumnWrapper> xAxisBox;
	private ComboBox<ColumnWrapper> yAxisBox;
	private Label xAxisErrorLabel;
	private Label yAxisErrorLabel;

	private boolean xSet = false;
	private boolean ySet = false;
	private boolean maxSet = false;
	private boolean minSet = false;

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

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
	 * Sets the Listener for the xAxis ComboBox.
	 * When the value changes, the data of the selected column will be added to the
	 * CategoryAxis of the barChart. An error message is shown to the user when the column
	 * does not contain appropriate values for a CategoryAxis.
	 */
	public void setXAxisEventListener() {
		xAxisBox.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
			xSet = false;
			xCol = newValue1.getColumn();
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
		yAxisBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			ySet = false;
			yCol = newValue.getColumn();
			DataDescriber<DataValue<?>> yColDescriber = new RowValueDescriber<>(yCol);
			try {
				NumberValue maxValue = (NumberValue) new Maximum(table, yColDescriber).calculate();
				NumberValue minValue = (NumberValue) new Minimum(table, yColDescriber).calculate();
				float max = Float.valueOf(maxValue.getValue().toString());
				float min = Float.valueOf(minValue.getValue().toString()) - 1;
				int sep = computeSeparatorValue(max, min);
				yAxis = new NumberAxis(yCol.getName(), min, max, sep);
				setErrorLabel(yAxisErrorLabel, "");
				ySet = true;
			} catch (ClassCastException e) {
				setErrorLabel(yAxisErrorLabel, "Something went wrong with the numbers.");
			} catch (InputMismatchException e) {
				setErrorLabel(yAxisErrorLabel, "Please select a column containing just numbers.");
			}
		});

	}

	/**
	 * Initialize the fxml objects for BarChartController.
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

	/**
	 * Called to initialize the barChartController object.
	 */
	@Override
	public void initialize() {
		initializeFields();
		setXAxisEventListener();
		setYAxisEventListener();
		vBox.getChildren().addAll(xAxisErrorLabel, xAxisBox, yAxisErrorLabel, yAxisBox);
	}

	/**
	 * Checks whether the two axes are set correctly.
	 *
	 * @return true if both axes contain correct data.
	 */
	@Override
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
					Float.valueOf(row.getValue(yCol).getValue().toString())));
		}
		res.getData().add(series1);
		res.setAnimated(false);
		return res;
	}

	/**
	 * create a writable image to print to the screen.
	 *
	 * @return writableImage object.
	 */
	@Override
	public WritableImage createImage() {
		BarChart chart = create();
		VBox box = new VBox();
		box.setMaxWidth(Double.MAX_VALUE);
		box.setMaxHeight(Double.MAX_VALUE);
		box.getChildren().add(chart);
		return box.snapshot(new SnapshotParameters(), new WritableImage(SIZE, SIZE));
	}
}