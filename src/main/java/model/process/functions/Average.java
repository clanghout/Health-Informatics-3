package model.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
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
	 * @param table
	 *            table of data
	 * @param argument
	 *            specified column
	 */
	public Average(DataTable table, DataDescriber<NumberValue> argument) {
		super(table, argument);
	}

	/**
	 * Calculate the average.
	 */
	@Override
	public NumberValue calculate() {
		if (!initialize()) {
			return new FloatValue(0f);
		}
		NumberValue sum;
		
		if (isInt()) {
			sum = (IntValue) new Sum(getTable(), getArgument()).calculate().getValue();
		} else {
			sum = (FloatValue) new Sum(getTable(), getArgument()).calculate();
		}
		float total = (float) sum.getValue();
		total = total / getTable().getRowCount();
		
		if (isInt()) {
			return new IntValue((int) total);
		}
		return new FloatValue(total);
	}
}