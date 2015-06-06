package model.process.functions;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;
import model.exceptions.InputMismatchException;

/**
 * This class will provide a framework for functions resulting single data values.
 * 
 * @author louisgosschalk 13-05-2015
 */
public abstract class Function {
	private DataRow row;
	private DataTable table;
	private DataDescriber<NumberValue> argument;

	public Function(DataTable model, DataDescriber<NumberValue> argument) {
		this.table = model;
		this.argument = argument;
	}

	/**
	 * Return the table.
	 * @return the table
	 */
	public DataTable getTable() {
		return table;
	}

	/**
	 * Return the DataDescriber for the function.
	 * @return the DataDescriber for the function
	 */
	public DataDescriber<NumberValue> getArgument() {
		return argument;
	}

	/**
	 * Set the table on which the function must perform.
	 * @param table table on which the funtion must perform
	 */
	public void setTable(DataTable table) {
		this.table = table.copy();
	}

	/**
	 * This abstract function will execute various arithmetic calculations.
	 * 
	 * @return DataValue
	 */
	public abstract DataValue calculate();

	/**
	 * Initialize class checks if specified column is eligible.
	 */
	protected boolean initialize() {

		if (table.getRowCount() == 0) {
<<<<<<< HEAD
			throw new InputMismatchException("Calculation of nothing does not exist");
=======
			return false;
>>>>>>> master
		}
		row = table.getRow(0);
		DataValue value = argument.resolve(row);

		if (!(value instanceof FloatValue) && !(value instanceof IntValue)) {
			throw new InputMismatchException("Specified column is neither float nor int");
		}
		return true;
	}

	/**
	 * This function returns a float value for the specified column if the column contains int or
	 * float.
	 * 
	 * @param arg
	 *            the specification of the column
	 * @param line
	 *            the specific row of the value
	 * @return float
	 */
	public float intOrFloat(DataDescriber<NumberValue> arg, DataRow line) {
		float result = 0.0f;
		if (arg.resolve(line) instanceof FloatValue) {
			result = (Float) argument.resolve(line).getValue();
		} else {
			result = (float) ((int) argument.resolve(line).getValue());
		}
		return result;
	}

}
