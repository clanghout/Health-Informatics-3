package controllers;

import controllers.visualizations.MatrixController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.data.DataModel;
import view.MatrixCreationDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the popup to create the state transition matrix.
 * Created by Chris on 10-6-2015.
 */
public class PopupMatrixController {
	private MatrixCreationDialog dialog;
	private VisualizationController visualizationController;
	private MatrixController matrixController;

	@FXML
	private ComboBox tableComboBox, columnComboBox;
	@FXML
	private VBox codesList;
	@FXML
	private Label createMessage;
	@FXML
	private Button makeButton;
	private Logger logger = Logger.getLogger("PopupMatrixController");

	public PopupMatrixController() {
	}

	public void initializeView(DataModel model,
	                           MatrixCreationDialog dialog,
	                           VisualizationController visualizationController) {
		this.dialog = dialog;
		this.visualizationController = visualizationController;
		createMessage.setMaxWidth(Double.MAX_VALUE);
		createMessage.setTextFill(Color.RED);
		matrixController = new MatrixController(model);
		logger.log(Level.INFO, "matrixController created");
		Set<String> codes = matrixController.getCodes();
		logger.log(Level.INFO, "list of codes = " + codes);
		if (codes.isEmpty()) {
			createMessage.setText("cannot create matrix when no codes are present.");
			makeButton.setDisable(true);
		}
		CheckBox codeBox;
		for (String code : codes) {
			codeBox = new CheckBox(code);
			codesList.getChildren().add(codeBox);
		}
	}

	/**
	 * Create matrix and send it to the visualizationController.
	 */
	@FXML
	protected void handleMatrixCreateButtonAction() {
		createMessage.setText("");
		List<String> selected = new ArrayList<>();
		if (!codesList.getChildren().isEmpty()) {
			for (Node box : codesList.getChildren()) {
				CheckBox cBox = (CheckBox) box;
				if (cBox.isSelected()) {
					selected.add(cBox.getText());
					logger.log(Level.INFO, cBox.getText() + "selected");
				}
			}
			if (selected.isEmpty()) {
				createMessage.setText("Please select one or more codes.");
			} else {
				logger.log(Level.INFO, "selected = " + selected);
				int[][] matrix = matrixController.create(selected);
				visualizationController.drawMatrix(matrix, selected);
				dialog.close();
			}
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
