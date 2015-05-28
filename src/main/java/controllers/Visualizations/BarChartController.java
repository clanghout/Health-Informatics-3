package controllers.Visualizations;

import controllers.VisualizationController;
import javafx.scene.control.ComboBox;
import model.data.DataColumn;
import model.data.DataTable;

/**
 * Controller for BarChart option in the visualization.
 * Fills the input VBOX in the FXML file.
 * Creates BarChart with selected columns in the input.
 * <p>
 * Created by Chris on 28-5-2015.
 */
public class BarChartController extends VisualizationController {
	private DataTable table;

	public BarChartController(DataTable table) {
		this.table = table;
	}

	public void initialize() {
		ComboBox<DataColumn> xAxisBox = new ComboBox<>();
		setColumnDropDown(xAxisBox, table);
	}
}