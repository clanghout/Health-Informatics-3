package controllers.popup;

import controllers.VisualizationController;
import controllers.visualizations.GraphImageController;
import controllers.visualizations.MatrixController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import view.MatrixCreationDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the popup to create the state transition matrix.
 * Created by Chris on 10-6-2015.
 */
public class PopupMatrixController extends PopupController {
	private MatrixCreationDialog dialog;
	private VisualizationController visualizationController;
	private MatrixController matrixController;

	@FXML
	private ComboBox<TableWrapper> tableComboBox;
	@FXML
	private ComboBox<GraphImageController.ColumnWrapper> columnComboBox;
	@FXML
	private VBox codesList;
	@FXML
	private Label createMessage;
	@FXML
	private Button makeButton;
	private Logger logger = Logger.getLogger("PopupMatrixController");
	private Map<DataTable, Set<String>> codes;
	private DataColumn column;

	public PopupMatrixController() {
	}

	public void initialize() {
		tableComboBox.setMaxWidth(Double.MAX_VALUE);
		columnComboBox.setMaxWidth(Double.MAX_VALUE);
		makeButton.setDisable(true);
		createMessage.setMaxWidth(Double.MAX_VALUE);
		createMessage.setTextFill(Color.RED);
		columnComboBox.setDisable(true);
	}

	public void initializeView(DataModel model,
	                           MatrixCreationDialog dialog,
	                           VisualizationController visualizationController) {
		this.dialog = dialog;
		this.visualizationController = visualizationController;
		matrixController = new MatrixController(model);
		initComboBox(model, tableComboBox);
		logger.log(Level.INFO, "matrixController created");
		codes = matrixController.getCodes();
		logger.log(Level.INFO, "list of codes = " + codes);
		if (matrixController.noCodes()) {
			noCodesAction(true);
		}
		columnComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				column = newValue.getColumn();
					makeButton.setDisable(false);
				}
		);
	}

	/**
	 * Actions to be done when no codes are present.
	 * @param tableDisable true if no code in whole program exists.
	 */
	private void noCodesAction(boolean tableDisable) {
		createMessage.setText("cannot create matrix when no codes are present.");
		makeButton.setDisable(true);
		tableComboBox.setDisable(tableDisable);
	}

	/**
	 * Adds new checkBoxes to the codesList.
	 */
	public void tableEvent() {
		if (codes.get(getTable()).isEmpty()) {
			noCodesAction(false);
		} else {
			createMessage.setText("");
			makeButton.setDisable(false);
			columnComboBox.setDisable(false);
			matrixController.setColumnDropDown(columnComboBox, getTable());
			codesList.getChildren().clear();
			CheckBox codeBox;
			for (String code : codes.get(getTable())) {
				codeBox = new CheckBox(code);
				codesList.getChildren().add(codeBox);
			}
		}

	}

	/**
	 * Create matrix and send it to the visualizationController.
	 */
	@FXML
	protected void handleMatrixCreateButtonAction() {
		createMessage.setText("");
		List<String> selected;
		if (!codesList.getChildren().isEmpty()) {
			selected = getSelected();
			if (selected.isEmpty()) {
				createMessage.setText("Please select one or more codes.");
			} else {
				logger.log(Level.INFO, "selected = " + selected);
				int[][] matrix = matrixController.create(selected, getTable(), column);
				visualizationController.drawMatrix(matrix, selected);
				dialog.close();
			}
		} else {
			dialog.close();
		}
	}

	/**
	 * Get all the selected codes in a list.
	 * @return list of selected codes.
	 */
	private List<String> getSelected() {
		List<String> selected = new ArrayList<>();
		for (Node box : codesList.getChildren()) {
			CheckBox cBox = (CheckBox) box;
			if (cBox.isSelected()) {
				selected.add(cBox.getText());
				logger.log(Level.INFO, cBox.getText() + "selected");
			}
		}
		return selected;
	}

	/**
	 * Close the dialog when cancel button is pressed in the popup.
	 */
	@FXML
	protected void handleCancelButtonAction() {
		dialog.close();
	}
}
