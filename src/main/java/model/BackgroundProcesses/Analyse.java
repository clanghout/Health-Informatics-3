package model.BackgroundProcesses;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.data.DataModel;
import model.exceptions.ParseException;
import model.input.reader.DataReader;
import model.language.Parser;
import model.process.DataProcess;
import org.parboiled.buffers.InputBuffer;
import org.parboiled.errors.ParseError;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jens on 6/17/15.
 */
public class Analyse implements Runnable {
	private String input;
	private DataModel model;
	private Logger logger = Logger.getLogger("Reader");
	private Label label;
	private VBox errorBox;

	private static final int ERROR_RANGE = 5;

	public Analyse(DataModel model, String input, Label errorLable, VBox errorBox) {
		this.model = model;
		this.input = input;
		this.label = errorLable;
		this.errorBox = errorBox;

	}

	@Override
	public void run() {
		Label errorLabelExtra = new Label();
		String text = "";
		try {
			Parser parser = new Parser();
			DataProcess process = parser.parse(input, model);
			process.process();
			model.setUpdated();
			logger.info("done analysing");
	} catch (IllegalArgumentException e) {
			text = "ERROR: " + e.getMessage();
	} catch (ParseException e) {
			text = e.getMessage();
		createParseExceptionMessage(e);
	} catch (UnsupportedOperationException e) {
			text = "ERROR: you are using an invalid operation";
	} catch (RuntimeException e) {
		text = "Runtime exception occurred";
		errorLabelExtra.setText(e.getLocalizedMessage() + " | " + e.getMessage());
	}
		final String finalText = text;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(finalText);
				errorBox.getChildren().add(errorLabelExtra);
			}
		});

	}


	/**
	 * Create a label with error message for every parse error.
	 *
	 * @param e teh Exception containing the parse errors
	 */
	private void createParseExceptionMessage(ParseException e) {
		List<ParseError> parseErrors = e.getParseErrors();
		for (ParseError error : parseErrors) {
			InputBuffer buffer = error.getInputBuffer();
			Label label = new Label();
			label.setMaxWidth(Double.MAX_VALUE);
			String beforeError = buffer.extract(0, error.getStartIndex());
			int lineCount = beforeError.length() - beforeError.replace("\n", "").length();
			String errorChar =
					buffer.extract(error.getStartIndex() - ERROR_RANGE,
							error.getEndIndex() + ERROR_RANGE);
			label.setText("Error at line "
					+ lineCount
					+ "; near \""
					+ errorChar + "\"");
			errorBox.getChildren().add(label);
		}
	}

}
