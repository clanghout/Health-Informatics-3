package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.data.DataModel;
import model.data.DataTable;
import javafx.scene.control.RadioButton;
import model.output.DataTableWriter;
import view.Dialog;
import view.SaveDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the save file wizard.
 * Created by Chris on 11-6-2015.
 */
public class SaveWizardController {
	@FXML
	private VBox tablesBox;
	@FXML
	private RadioButton extensionTxt, extensionCsv;
	@FXML
	private RadioButton delimiterComma, delimiterTab, delimiterSpace;
	@FXML
	private Label saveMessage;

	private DataModel model;
	private Dialog dialog;
	private Parent root;
	private ToggleGroup extension;
	private ToggleGroup delimiter;

	private Logger logger = Logger.getLogger("SaveWizardController");

	private static final int EXTENSION_SIZE = 4;

	public SaveWizardController() {
	}

	public void initializeView(DataModel model,
	                           SaveDialog dialog,
	                           Parent root) {
		saveMessage.setTextFill(Color.RED);
		extension = new ToggleGroup();
		extensionTxt.setToggleGroup(extension);
		extensionCsv.setToggleGroup(extension);
		extensionTxt.setSelected(true);

		delimiter = new ToggleGroup();
		delimiterComma.setToggleGroup(delimiter);
		delimiterSpace.setToggleGroup(delimiter);
		delimiterTab.setToggleGroup(delimiter);
		delimiterComma.setSelected(true);

		setUserData();

		extension.selectedToggleProperty().addListener(
				(observable, oldValue, newValue) -> {
					if (newValue.equals(extensionCsv)) {
						delimiterComma.setSelected(true);
						setDisableDelimiter(true);
					} else {
						setDisableDelimiter(false);
					}
				}
		);

		this.root = root;
		this.dialog = dialog;
		this.model = model;
		for (DataTable table : model.getObservableList()) {
			tablesBox.getChildren().add(new CheckBox(table.getName()));
		}
	}

	private void setUserData() {
		extensionTxt.setUserData("txt");
		extensionCsv.setUserData("csv");
		delimiterComma.setUserData("comma");
		delimiterSpace.setUserData("space");
		delimiterTab.setUserData("tab");
	}

	private void setDisableDelimiter(Boolean value) {
		delimiterComma.setDisable(value);
		delimiterSpace.setDisable(value);
		delimiterTab.setDisable(value);
	}

	private List<String> getSelectedTables() {
		List<String> res = new ArrayList<>();
		for (Node tableNode : tablesBox.getChildren()) {
			CheckBox tableBox = (CheckBox) tableNode;
			if (tableBox.isSelected()) {
				res.add(tableBox.getText());
			}
		}
		return res;
	}

	@FXML
	protected void handleSaveButton() {
		List<String> tables = getSelectedTables();
		String delimitSymbol = ", ";
		String selectedExtension = extension.getSelectedToggle().getUserData().toString();
		if (selectedExtension.equals("txt")) {
			switch (delimiter.getSelectedToggle().getUserData().toString()) {
				case "tab":
					delimitSymbol = "\t";
					break;
				case "space":
					delimitSymbol = " ";
				default:
					break;
			}
		}
		if (!tables.isEmpty()) {
			FileChooser fileChooser = new FileChooser();

			fileChooser.setTitle("Select location to save output");
			fileChooser.setInitialDirectory(
					new File(System.getProperty("user.home"))
			);
			fileChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter(
							selectedExtension.toUpperCase(), "*." + selectedExtension)
			);
			File temp = fileChooser.showSaveDialog(root.getScene().getWindow());
			String tempPath = temp.getAbsolutePath();
			if (tempPath.endsWith(selectedExtension)) {
				temp = new File(tempPath.substring(
						0, tempPath.length() - EXTENSION_SIZE));
			}
			DataTableWriter dataTableWriter = new DataTableWriter();
			try {
				for (String tableName : tables) {
					DataTable table = model.getByName(tableName).get();
					System.out.println("temp.getPath() = " + temp.getPath());
					File saveLocation = new File(
							temp.getPath() + "_" + table.getName()
									+ "." + selectedExtension);
					dataTableWriter.write(table, saveLocation, delimitSymbol);
				}
				dialog.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error saving", e);
				saveMessage.setText(
						"Data not saved! An error occurred while saving the file.");
			}
		} else {
			logger.log(Level.INFO, "No table selected thus no save");
			saveMessage.setText("Please select one or more tables to save.");
		}
	}

	@FXML
	protected void handleCancelButton() {
		dialog.close();
	}
}