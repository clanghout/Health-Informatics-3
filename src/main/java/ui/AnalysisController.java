package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import language.Parser;
import model.data.DataModel;
import model.data.process.DataProcess;


/**
 * The controller for the Analysis tab.
 *
 * Created by Boudewijn on 22-5-2015.
 */
public class AnalysisController {

	private DataModel model;

	@FXML
	private TextArea languageText;

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
		DataProcess process = parser.parse(languageText.getText());

		process.process();
	}
}
