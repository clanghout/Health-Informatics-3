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
	private boolean isInt;

	public Function(DataTable table, DataDescriber<NumberValue> argument) {
		this.table = table;
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
		this.table = table;
	}

	/**
	 * This abstract function will execute various arithmetic calculations.
	 * 
	 * @return DataValue The result of the calculation.
	 */
	public abstract NumberValue calculate();

	/**
	 * Initialize class checks if specified column is eligible.
	 */
	protected boolean initialize() {

		if (table.getRowCount() == 0) {
			return false;
		}
		row = table.getRow(0);
		DataValue value = argument.resolve(row);

		if (value instanceof IntValue) {
			setInt(true);
		} else if (value instanceof FloatValue) {
			setInt(false);
		} else {
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
	 * @return float The value as a float.
	 */
	protected float intOrFloat(DataDescriber<NumberValue> arg, DataRow line) {
		float result = 0.0f;
		if (arg.resolve(line) instanceof FloatValue) {
			result = (Float) argument.resolve(line).getValue();
			setInt(false);
		} else {
			result = (float) ((int) argument.resolve(line).getValue());
			setInt(true);
		}
		return result;
	}
	
	/**
	 * If the input is an integer this is true.
	 * @param set
	 */
	protected void setInt(boolean set) {
		isInt = set;
	}
	
	/**
	 * Checks whether or not the input is an integer.
	 * @return
	 */
	protected boolean isInt() {
		return isInt;
	}

}
