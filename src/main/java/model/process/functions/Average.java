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
public class Average extends Function<NumberValue<?>> {

	/**
	 * Construct a new average.
	 * 
	 * @param table
	 *            table of data
	 * @param argument
	 *            specified column
	 */
	public Average(DataTable table, DataDescriber<NumberValue<?>> argument) {
		super(table, argument);
	}

	/**
	 * Calculate the average.
	 */
	@Override
	public FloatValue calculate() {
		if (!initialize()) {
			return new FloatValue(0f);
		}
		NumberValue sum = new Sum(getTable(), getArgument()).calculate();
		float total;
		if (isInt()) {
			total = (float) ((int) sum.getValue());
		} else {
			total = (float) sum.getValue();
		}
		total = total / getTable().getRowCount();
		return new FloatValue(total);
	}
}