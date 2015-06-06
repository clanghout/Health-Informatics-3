package model.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

/**
 * Function for finding the average in a specified column.
 * 
 * @author Louis Gosschalk 13-05-2015
 */
public class Average extends Function {

	/**
	 * Construct a new average.
	 * 
	 * @param model
	 *            table of data
	 * @param argument
	 *            specified column
	 */
	public Average(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
	}

	/**
	 * Calculate the average.
	 */
	@Override
	public FloatValue calculate() {
		if (!initialize()) {
			return new FloatValue(0);
		}
		FloatValue sum = new Sum(getTable(), getArgument()).calculate();
		float total = sum.getValue();
		total = total / getTable().getRowCount();
		return new FloatValue(total);
	}
}