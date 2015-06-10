package controllers;

import controllers.visualizations.MatrixController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.data.DataModel;
import view.MatrixCreationDialog;

import java.util.Set;

/**
 * Controller for the popup to create the state transition matrix.
 * Created by Chris on 10-6-2015.
 */
public class PopupMatrixController {
	private DataModel model;
	private MatrixCreationDialog dialog;
	private VisualizationController visualizationController;
	private MatrixController matrixController;

	@FXML
	private VBox codesList;
	@FXML
	private Label createMessage;

	public PopupMatrixController() {
	}

	public void initializeView(DataModel model,
	                        MatrixCreationDialog dialog,
	                        VisualizationController visualizationController) {
		this.model = model;
		this.dialog = dialog;
		this.visualizationController = visualizationController;
		createMessage.setMaxWidth(Double.MAX_VALUE);
		MatrixController matrixController = new MatrixController(model);
		CheckBox codeBox;
		Set<String> codes = matrixController.getCodes();
		codes.add("klaas");
		codes.add("sjon");
		codes.add("jacco");
		for(String code : codes) {
			codeBox = new CheckBox(code);
			codesList.getChildren().add(codeBox);
		}
	}

	/**
	 * Create matrix and send it to the visualizationController.
	 */
	@FXML
	protected void handleMatrixCreateButtonAction() {
		if (true) {
			createMessage.setTextFill(Color.RED);
			createMessage.setText("Error message hier");
		} else {
			dialog.close();
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
