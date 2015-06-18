package controllers;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.data.DataModel;
import model.input.reader.DataReader;
import model.input.reader.XmlReader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jens on 6/18/15.
 */
public class Reader {
	private DataModel model;
	private MainUIController mainUIController;

	private Label errorLabel;

	private File file;
	private Logger logger = Logger.getLogger("Reader");

	public Reader(File file, MainUIController mainUIController, Label error) {
		this.file = file;
		this.mainUIController = mainUIController;
		this.errorLabel = error;
		this.model = new DataModel();
	}

	public void execute() {

		mainUIController.setModel(model);
		try {
			errorLabel.setText("");
			Task task = createTask();
			setHandlers(task);

			if (!mainUIController.startBackgroundProcess(task, "Loading Data")) {
				errorLabel.setText("An operation is already running in the background");
			}


		} catch (Exception e) {
			logger.log(Level.WARNING, "An error occurred while reading the file", e);
		}

	}

	/**
	 * Set the callback function for when the task fails or succeeds.
	 * @param task the task that must get the callback functions.
	 */
	private void setHandlers(Task task) {

		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override public void handle(WorkerStateEvent t) {
				Throwable exception = task.getException();
				logger.log(Level.WARNING, "An error occurred while reading the file"
								+ exception.getClass().getName() + " -> "
								+ exception.getMessage(),
						exception.getCause());
				mainUIController.endBackgroundProcess(false);
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("An error occurred while reading the file");
			}
		});

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override public void handle(WorkerStateEvent t) {
				mainUIController.setModel(model);
				model.setUpdated();
				mainUIController.endBackgroundProcess(true);
			}
		});
	}

	/**
	 * Create a task for the analysis.
	 * @return a new task that can perform an analysis.
	 */
	private Task createTask() {
		return new Task() {
			@Override protected Integer call() throws Exception {
				DataReader reader = new DataReader(new XmlReader());
				reader.read(file);
				model = reader.createDataModel();

				return null;
			}
		};
	}
}
