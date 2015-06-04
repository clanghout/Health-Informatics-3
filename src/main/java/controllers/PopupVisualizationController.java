package controllers;

import controllers.visualizations.BarChartController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import model.data.DataModel;
import model.data.DataTable;

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
	private DataTable table;
	private BarChartController bcc;

	public PopupVisualizationController() {

	}

	/**
	 * Initialize the popup window.
	 * initially disables comboboxes, reenabled when data is present.
	 * <p>
	 * This method is automatically called at the initialization of the application.
	 */
	public void initialize() {
		tableComboBox.setMaxWidth(Double.MAX_VALUE);
		visualizationComboBox.setMaxWidth(Double.MAX_VALUE);
		tableComboBox.setDisable(true);
		visualizationComboBox.setDisable(true);
	}

	/**
	 * Manual initialization of the controller for when the DataModel is specified.
	 *
	 * @param model the input model of wich the table can be specified.
	 */
	public void initializeView(DataModel model) {
		tableComboBox.setDisable(false);
		tableComboBox.setItems(model.getObservableList());
		tableComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			this.table = newValue;
			visualizationComboBox.setDisable(false);
		});
		visualizationComboBox.setItems(FXCollections.observableArrayList(
				"BarChart", "temp"));
		visualizationComboBox.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					visualizationInputVBox.getChildren().clear();
					switch (newValue) {
						case "BarChart":
							bcc = new BarChartController(table, visualizationInputVBox);
							bcc.initialize();
							break;
						default:
							break;
					}
				});


	}

	@FXML
	protected void handleGraphCreateButtonAction(ActionEvent event) {

	}

	@FXML
	protected void handleCancelButtonAction(ActionEvent event) {

	}
}
