package model.data.process;

import model.data.DataTable;

/**
 * This class defines a data process.
 *
 * This class provides the framework for all data processes in our
 * analysis framework.
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class DataProcess {

	private DataTable input;
	private DataTable output;

	/**
	 * Runs this process.
	 *
	 * @return The data model resulting from running this process.
	 */
	public final DataTable process() {
		DataTable model = doProcess();
		if (model == null) {
			throw new NullPointerException("A process should always result in an output");
		}
		output = model;
		return model;
	}

	/**
	 * Subclasses should override this to implement the data process.
	 *
	 * @return The output of the data process
	 */
	protected abstract DataTable doProcess();

	/**
	 * Set the input for the data process.
	 * Note: Setting an input isn't required.
	 * @param input The input for the data process
	 */
	public final void setInput(DataTable input) {
		this.input = input;
	}

	/**
	 * Get the input for the data process.
	 *
	 * @return The input for this data process or null if none is set.
	 */
	public final DataTable getInput() {
		return input;
	}

	/**
	 * Get the output for the data process.
	 *
	 * @return The output of the data process
	 */
	public final DataTable getOutput() {
		return output;
	}
}
