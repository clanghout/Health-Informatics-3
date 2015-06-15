package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
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
	private RadioButton dateUnix, dateExcel, dateSelf;
	@FXML
	private RadioButton saveNames, saveNew;
	@FXML
	private RadioButton nullLower, nullUpper, nullEmpty;
	@FXML
	private Label saveMessage;
	@FXML
	private Parent root;
	@FXML
	private Label tableSaveLabel;

	private DataModel model;
	private Dialog dialog;
	private ToggleGroup extension;
	private ToggleGroup delimiter;
	private ToggleGroup dateFormat;
	private ToggleGroup saveName;
	private ToggleGroup nullSave;

	private Logger logger = Logger.getLogger("SaveWizardController");

	private static final int EXTENSION_SIZE = 4;

	/**
	 * Constructor for controller.
	 * Needed for fxml.
	 */
	public SaveWizardController() {
	}

	/**
	 * Set the initialization of the popup.
	 *
	 * @param model  The dataModel of the program.
	 * @param dialog The dialog where this is the controller of.
	 */
	public void initializeView(DataModel model,
	                           SaveDialog dialog) {

		this.dialog = dialog;
		this.model = model;
		saveMessage.setTextFill(Color.RED);

		setToggleGroups();
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

		for (DataTable table : model.getObservableList()) {
			tablesBox.getChildren().add(new CheckBox(table.getName()));
		}
	}

	/**
	 * Set the groups of the radioButtons.
	 */
	private void setToggleGroups() {
		extension = new ToggleGroup();
		extensionTxt.setToggleGroup(extension);
		extensionCsv.setToggleGroup(extension);
		extensionTxt.setSelected(true);

		delimiter = new ToggleGroup();
		delimiterComma.setToggleGroup(delimiter);
		delimiterSpace.setToggleGroup(delimiter);
		delimiterTab.setToggleGroup(delimiter);
		delimiterComma.setSelected(true);

		dateFormat = new ToggleGroup();
		dateExcel.setToggleGroup(dateFormat);
		dateUnix.setToggleGroup(dateFormat);
		dateSelf.setToggleGroup(dateFormat);
		dateSelf.setDisable(true);
		dateUnix.setSelected(true);

		saveName = new ToggleGroup();
		saveNames.setToggleGroup(saveName);
		saveNew.setToggleGroup(saveName);
		saveNew.setSelected(true);

		nullSave = new ToggleGroup();
		nullLower.setToggleGroup(nullSave);
		nullUpper.setToggleGroup(nullSave);
		nullEmpty.setToggleGroup(nullSave);
		nullUpper.setSelected(true);
	}

	/**
	 * Set the user data for the RadioButtons.
	 * This is the content of the RadioButton
	 * because for some reason the text is not saved.
	 */
	private void setUserData() {
		extensionTxt.setUserData("txt");
		extensionCsv.setUserData("csv");

		delimiterComma.setUserData("comma");
		delimiterSpace.setUserData("space");
		delimiterTab.setUserData("tab");

		dateUnix.setUserData("unix");
		dateExcel.setUserData("excel");
		//TODO: let user specify date format;
		dateSelf.setUserData("TBD");

		saveNames.setUserData(false);
		saveNew.setUserData(true);

		nullEmpty.setUserData("empty");
		nullLower.setUserData("lower");
		nullUpper.setUserData("upper");

	}

	/**
	 * Disable or enable the delimiter chooser.
	 *
	 * @param value True for disable, false for enable.
	 */
	private void setDisableDelimiter(Boolean value) {
		delimiterComma.setDisable(value);
		delimiterSpace.setDisable(value);
		delimiterTab.setDisable(value);
	}

	/**
	 * Check which tables are selected by the user.
	 *
	 * @return A list containing the names of the tables the user selected.
	 */
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

	/**
	 * Handler for the save button.
	 */
	@FXML
	protected void handleSaveButton() {
		tableSaveLabel.setTextFill(Color.BLACK);
		List<String> tables = getSelectedTables();
		String selectedExtension = extension.getSelectedToggle().getUserData().toString();
		String delimitSymbol = getSelectedDelimiter();
		if (!tables.isEmpty()) {
			File temp;
			boolean newSave = (boolean) saveName.getSelectedToggle().getUserData();
			try {
				if (newSave) {
					temp = selectNewFileLocation(selectedExtension);
					String tempPath = temp.getAbsolutePath();
					if (tempPath.endsWith(selectedExtension)) {
						temp = new File(tempPath.substring(
								0, tempPath.length() - EXTENSION_SIZE));
					}
				} else {
					temp = selectFolderLocation();
				}

				writeTables(tables, temp, selectedExtension, delimitSymbol, newSave);
			} catch (NullPointerException e) {
				logger.log(Level.SEVERE, "Error saving; no file selected");
				saveMessage.setText(
						"Data not saved! No file location selected.");
			}
		} else {
			logger.log(Level.INFO, "No table selected thus no save");
			tableSaveLabel.setTextFill(Color.RED);
			saveMessage.setText("Please select one or more tables to save.");
		}
	}

	/**
	 * Write the selected tables to file.
	 *
	 * @param tables    the names of the tables to write.
	 * @param location  the location where to save.
	 * @param extension the extension for the save.
	 * @param delimiter the delimiter to use in the file.
	 */
	private void writeTables(List<String> tables,
	                         File location,
	                         String extension,
	                         String delimiter,
	                         boolean underScore) {
		DataTableWriter dataTableWriter = new DataTableWriter();
		try {
			for (String tableName : tables) {
				DataTable table = model.getByName(tableName).get();
				File saveLocation;
				if (underScore) {
					saveLocation = new File(
							location.getPath() + "_" + table.getName()
									+ "." + extension);
				} else {
					saveLocation = new File(
							location.getPath() + File.separator + table.getName()
									+ "." + extension);
				}
				dataTableWriter.write(table, saveLocation, delimiter);
			}
			dialog.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error saving", e);
			saveMessage.setText(
					"Data not saved! An error occurred while saving the file.");
		}
	}

	/**
	 * Open a fileChooser to select the save location.
	 *
	 * @param extension the selected extension
	 * @return picked location to save the file.
	 */
	private File selectNewFileLocation(String extension) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select name to save the data");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter(
						extension.toUpperCase(), "*." + extension)
		);
		return fileChooser.showSaveDialog(root.getScene().getWindow());
	}

	private File selectFolderLocation() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select location to save output");
		directoryChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		return directoryChooser.showDialog(root.getScene().getWindow());
	}

	/**
	 * Check what delimiter is selected by the user.
	 * If The user selected csv as extension the selected value will always be comma.
	 *
	 * @return the delimiter to use.
	 */
	private String getSelectedDelimiter() {
		String res = ", ";
		switch (delimiter.getSelectedToggle().getUserData().toString()) {
			case "tab":
				res = "\t";
				break;
			case "space":
				res = " ";
				break;
			default:
				break;
		}
		return res;
	}

	/**
	 * Cancel button; closes dialog.
	 */
	@FXML
	protected void handleCancelButton() {
		dialog.close();
	}
}