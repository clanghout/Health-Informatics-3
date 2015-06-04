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
 * abc.
 * Created by Chris on 2-6-2015.
 */
public class PopupVisualizationController {
	private DataModel model;
	private DataTable table;
	@FXML
	private ComboBox<DataTable> tableComboBox;
	@FXML
	private ComboBox<String> visualizationComboBox;
	@FXML
	private VBox visualizationInputVBox;
	private BarChartController bcc;

	public PopupVisualizationController() {

	}

	/**
	 * set items of table to all the tables in the model.
	 */
	public void initialize() {
		tableComboBox.setMaxWidth(Double.MAX_VALUE);
		visualizationComboBox.setMaxWidth(Double.MAX_VALUE);
		tableComboBox.setDisable(true);
		visualizationComboBox.setDisable(true);
	}

	public void initializeView(DataModel model) {
		this.model = model;
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
