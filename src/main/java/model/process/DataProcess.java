package model.process;

import model.data.DataModel;
import model.data.Table;

/**
 * This class defines a data process.
 *
 * This class provides the framework for all data processes in our
 * analysis framework.
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class DataProcess {

	private Table input;
	private Table output;

	private DataModel model;

	/**
	 * Runs this process.
	 *
	 * @return The data table resulting from running this process.
	 */
	public final Table process() {
		Table table = doProcess();
		if (table == null) {
			throw new NullPointerException("A process should always result in an output");
		}
		output = table;
		return table;
	}

	/**
	 * Subclasses should override this to implement the data process.
	 *
	 * @return The output of the data process
	 */
	protected abstract Table doProcess();

	/**
	 * Set the input for the data process.
	 * Note: Setting an input isn't required.
	 * @param input The input for the data process
	 */
	public final void setInput(Table input) {
		this.input = input;
	}

	/**
	 * Get the input for the data process.
	 *
	 * @return The input for this data process or null if none is set.
	 */
	public final Table getInput() {
		return input;
	}

	/**
	 * Get the output for the data process.
	 *
	 * @return The output of the data process
	 */
	public final Table getOutput() {
		return output;
	}

	/**
	 * Set the DataModel for this process.
	 *
	 * @param model The model for this process.
	 */
	public final void setDataModel(DataModel model) {
		this.model = model;
	}

	/**
	 * Get the DataModel for this process.
	 *
	 * @return The model for this process.
	 */
	public final DataModel getDataModel() {
		return model;
	}
}
