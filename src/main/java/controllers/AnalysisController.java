package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.exceptions.ParseException;
import model.language.Parser;
import model.data.DataModel;
import model.process.DataProcess;


/**
 * The controller for the Analysis tab.
 *
 * Created by Boudewijn on 22-5-2015.
 */
public class AnalysisController {

	private DataModel model;

	@FXML
	private TextArea userscript;

	/**
	 * Sets the model that will be observed.
	 * @param model The model
	 */
	public void setDataModel(DataModel model) {
		this.model = model;
	}

	@FXML
	protected void handleExecuteButtonAction(ActionEvent event) {
		Parser parser = new Parser();

		try {
			DataProcess process = parser.parse(userscript.getText(), model);
			process.process();
			model.setUpdated();
		} catch (ParseException e) {

		}
	}
}
