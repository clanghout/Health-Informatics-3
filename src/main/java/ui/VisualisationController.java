package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import model.data.DataModel;

import java.util.logging.Logger;

/**
 * Control visualisation.
 * Created by Chris on 26-5-2015.
 */
public class VisualisationController {
	@FXML
	private ComboBox<String> visualisation;
	@FXML
	private ComboBox table;

	private DataModel model;
	private MainUIController mainUIController;
	private Logger logger = Logger.getLogger("VisualisationController");

	public VisualisationController() {
	}

	public void initialize(MainUIController mainUIController) {
		this.mainUIController = mainUIController;

		System.out.println("visualisation = " + visualisation);
		visualisation.setItems(FXCollections.observableArrayList(
				"Frequency graph", "BoxPlot"));

	}

}
