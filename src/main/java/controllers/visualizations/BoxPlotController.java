package controllers.visualizations;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.NumberValue;
import model.process.functions.Maximum;
import model.process.functions.Minimum;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.embed.swing.SwingFXUtils.toFXImage;

/**
 * Controller for the BoxPlot.
 * This will create an image of the JFreeChart BoxPlot with our table data
 * and send it to the Visualization tab to display.
 * <p>
 * Created by Chris on 7-6-2015.
 */
public class BoxPlotController extends ChartController {
	private DataTable table;
	private VBox vBox;
	private DataColumn yCol;
	private ComboBox<DataColumn> yAxisBox;
	private Label yAxisErrorLabel;

	private boolean ySet = false;

	private NumberAxis yAxis;

	public static final double SCALEUP_YAXIS = 1.01;
	public static final double SCALEDOWN_YAXIS = 0.99;
	public static final int WIDTH = 150;

	/**
	 * create new BoxPlot controller.
	 *
	 * @param table containing data for the BoxPlot.
	 * @param vBox  placement box for the input boxes and labels.
	 */
	public BoxPlotController(DataTable table, VBox vBox) {
		this.table = table;
		this.vBox = vBox;
	}

	/**
	 * Initialize the fxml objects for BoxPlotController.
	 */
	public void initializeFields() {
		yAxisBox = new ComboBox<>();
		yAxisBox.setPromptText("y-Axis");
		yAxisErrorLabel = new Label();
		yAxisErrorLabel.setMaxWidth(Double.MAX_VALUE);
		setColumnDropDown(yAxisBox, table);
	}

	/**
	 * initialize the BoxPlot controller.
	 */
	@Override
	public void initialize() {
		initializeFields();
		setYAxisEventListener();
		vBox.getChildren().addAll(yAxisErrorLabel, yAxisBox);
	}

	/**
	 * Check if y axis contains valid data.
	 *
	 * @return true if axis is correct.
	 */
	@Override
	public boolean axesSet() {
		return ySet;
	}

	/**
	 * Sets the Listener for the yAxis ComboBox.
	 * The NumberAxis is created and the scale is set.
	 */
	public void setYAxisEventListener() {
		yAxisBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			ySet = false;
			yCol = newValue;
			DataDescriber<NumberValue> yColDescriber = new RowValueDescriber<>(yCol);
			try {
				float max = new Maximum(table, yColDescriber).calculate().getValue();
				float min = new Minimum(table, yColDescriber).calculate().getValue() - 1;
				yAxis = new NumberAxis(newValue.getName());
				yAxis.setRange(min * SCALEDOWN_YAXIS, max * SCALEUP_YAXIS);
				setErrorLabel(yAxisErrorLabel, "");
				ySet = true;
			} catch (Exception e) {
				setErrorLabel(yAxisErrorLabel, "Please select a column with number values.");
			}
		});
	}

	/**
	 * create the dataSet for the boxPlot.
	 *
	 * @return dataSet containing the data for the boxPlot.
	 */
	public DefaultBoxAndWhiskerCategoryDataset createDataset() {
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		List data = table.getRows().stream()
				.map(row -> row.getValue(yCol).getValue()).collect(Collectors.toList());
		dataset.add(data, "", table.getName());
		return dataset;
	}

	/**
	 * Create an image out of the dataSet.
	 * First, a renderer is created. With that renderer a plot is made
	 * and a chart is added to the plot. Then an image is created.
	 *
	 * @return WritableImage of the BoxPlot.
	 */
	@Override
	public WritableImage createImage() {
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		renderer.setMeanVisible(false);
		renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
		final CategoryPlot plot =
				new CategoryPlot(createDataset(),
						new CategoryAxis(table.getName()), yAxis, renderer);
		final JFreeChart chart = new JFreeChart(
				"",
				new Font("SansSerif", Font.BOLD, 14),
				plot,
				false
		);
		WritableImage image = new WritableImage(WIDTH + WIDTH, SIZE);
		toFXImage(chart.createBufferedImage(WIDTH, SIZE), image);
		return image;
	}
}
