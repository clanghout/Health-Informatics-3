package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import view.GraphCreationDialog;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Control visualisation.
 * Created by Chris on 26-5-2015.
 */
public class VisualizationController {
	@FXML
	private VBox visualizationGraph;
	@FXML
	private Button makeGraphButton;
	@FXML
	private Button clearViewButton;
	@FXML
	private Button saveButton;
	@FXML
	private PopupVisualizationController popupVisualizationController;

	private DataModel model;

	private Logger logger = Logger.getLogger("VisualizationController");

	/**
	 * Constructor for Visualization controller.
	 */
	public VisualizationController() {
	}

	/**
	 * Initialization module for the visualisation controller.
	 * This method is automatically called by javaFX on initialization.
	 * The dropDown menu's are disabled while no DataModel is loaded.
	 */
	public void initialize() {
		makeGraphButton.setDisable(true);
		clearViewButton.setDisable(true);
		saveButton.setDisable(true);
	}

	/**
	 * Init method after a model is read.
	 */
	public void initializeVisualisation() {
		makeGraphButton.setDisable(false);
		clearViewButton.setDisable(false);
	}

	public void drawGraph(Chart chart) {
		visualizationGraph.getChildren().add(chart);
	}

	/**
	 * Sets the model that will be observed.
	 *
	 * @param model The model
	 */
	public void setModel(DataModel model) {
		this.model = model;
	}

	@FXML
	protected void handlePopupButtonAction(ActionEvent event) {
		visualizationGraph.getChildren().clear();
		try {
			GraphCreationDialog gcd = new GraphCreationDialog();
			gcd.show();
			popupVisualizationController = gcd.getFxml().getController();
			popupVisualizationController.initializeView(model, this);
		} catch (NullPointerException e) {
			System.out.println("no controller");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "FXML file cannot be loaded");
		}
	}

	@FXML
	protected void handleSaveButtonAction(ActionEvent event) {

	}

	@FXML
	protected void handleClearButtonAction(ActionEvent event) {
		visualizationGraph.getChildren().clear();
	}
}
