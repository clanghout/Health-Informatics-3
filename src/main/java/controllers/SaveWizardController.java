package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import model.data.DataModel;
import model.data.DataTable;

/**
 * Controller for the save file wizard.
 * Created by Chris on 11-6-2015.
 */
public class SaveWizardController {
	@FXML
	private VBox tables;
	private DataModel model;

	public SaveWizardController(DataModel model) {
		this.model = model;
		initializeView();
	}

	private void initializeView() {
		for (DataTable table : model.getObservableList()) {
			tables.getChildren().add(new CheckBox(table.getName()));
		}
	}
}

