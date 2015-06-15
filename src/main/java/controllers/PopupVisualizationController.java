package controllers;

import controllers.visualizations.BarChartController;
import controllers.visualizations.BoxPlotController;
import controllers.visualizations.ChartController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.data.DataModel;
import model.data.DataTable;
import view.GraphCreationDialog;

/**
 * Controller for the popup window that shows when the graph create button is pressed.
 * With this controller the right visualisation is chosen and the input needed for
 * this visualisation.
 * <p>
 * Created by Chris on 2-6-2015.
 */
public class PopupVisualizationController {
	@FXML
	private ComboBox<DataTable> tableComboBox;
	@FXML
	private ComboBox<String> visualizationComboBox;
	@FXML
	private VBox visualizationInputVBox;
	@FXML
	private Label createError;
	private DataTable table;
	private ChartController chartController;
	private VisualizationController visualizationController;
	private GraphCreationDialog dialog;

	public PopupVisualizationController() {
	}

	/**
	 * Initialize the popup window.
	 * initially disables comboBoxes, re enabled when data is present.
	 * <p>
	 * This method is automatically called at the initialization of the application.
	 */
	public void initialize() {
		tableComboBox.setMaxWidth(Double.MAX_VALUE);
		visualizationComboBox.setMaxWidth(Double.MAX_VALUE);
		tableComboBox.setDisable(true);
		visualizationComboBox.setDisable(true);
		createError.setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 * Manual initialization of the controller for when the DataModel is specified.
	 *
	 * @param model the input model of which the table can be specified.
	 */
	public void initializeView(
			DataModel model,
			VisualizationController visualisationController,
			GraphCreationDialog dialog) {
		this.visualizationController = visualisationController;
		this.dialog = dialog;

		tableComboBox.setDisable(false);
		tableComboBox.setItems(model.getObservableList());
		tableComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			this.table = newValue;
			visualizationComboBox.setDisable(false);
		});
		visualizationComboBox.setItems(FXCollections.observableArrayList(
				"BarChart", "BoxPlot", "Empty"));
		visualizationComboBox.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					visualizationInputVBox.getChildren().clear();
					switch (newValue) {
						case "BarChart":
							chartController =
									new BarChartController(table, visualizationInputVBox);
							chartController.initialize();
							break;
						case "BoxPlot":
							chartController =
									new BoxPlotController(table, visualizationInputVBox);
							chartController.initialize();
							break;
						default:
							visualizationInputVBox.getChildren().clear();
							break;
					}
				});
	}

	/**
	 * Create a graph if possible and send it to the visualization controller.
	 */
	@FXML
	protected void handleGraphCreateButtonAction() {
		if (chartController.axesSet()) {
			if (chartController instanceof BarChartController) {
				BarChart chart = ((BarChartController) chartController).create();
				chart.snapshot(new SnapshotParameters(), null);
				visualizationController.drawGraph(chart);
			} else {
				WritableImage image = chartController.createImage();
				visualizationController.drawImage(image);
			}
			dialog.close();
		} else {
			createError.setTextFill(Color.RED);
			createError.setText("Could not create graph; axes not fully set.");
		}
	}

	/**
	 * Close the dialog when cancel button is pressed in the popup.
	 */
	@FXML
	protected void handleCancelButtonAction() {
		dialog.close();
	}
}
