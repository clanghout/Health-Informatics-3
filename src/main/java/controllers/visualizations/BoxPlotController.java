package controllers.visualizations;


import javafx.scene.chart.Chart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
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
	private NumberAxis yAxis;


	public static final double SCALEUP_YAXIS = 1.01;
	public static final double SCALEDOWN_YAXIS = 0.99;

	/**
	 * create new BoxPlot controller.
	 *
	 * @param table containing data for the BoxPlot.
	 * @param vBox  placement box for the input boxes and labels.
	 */
	public BoxPlotController(DataTable table, VBox vBox) {
		super(table, vBox);
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
		xSet = true;
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
	 * cannot use this method because javaFX return is expected.
	 *
	 * @return null
	 */
	public Chart create() {
		// Ik weet niet goed hoe ik dit kan oplossen omdat het ene dus javaFX gebruikt en het andere
		// JFreeChart. De javaFX elementen kunnen gewoon getekend worden in de Visualization tab
		// alleen van de FreeChart moet dus een image gemaakt worden die dan weer
		// getekend kan worden.
		return null;
	}

	/**
	 * create the dataSet for the boxPlot.
	 * @return dataSet containing the data for the boxPlot.
	 */
	public DefaultBoxAndWhiskerCategoryDataset createDataset() {
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		List data = table.getRows().stream()
				.map(row -> row.getValue(yCol).getValue()).collect(Collectors.toList());
		dataset.add(data, "", table.getName());
		return dataset;
	}

	public WritableImage createImage() {
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
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
		WritableImage image = new WritableImage(SIZE, SIZE);
		toFXImage(chart.createBufferedImage(SIZE, SIZE), image);
		System.out.println("image = " + image);
		return image;
	}


}
