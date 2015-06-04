package model.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

/**
 * A class for calculating the sum of a specified column over all rows.
 * 
 * @author louisgosschalk 16-05-2015
 */
public class Sum extends Function {

	public Sum(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
	}

	/**
	 * This function calculates the sum of a column.
	 */
	@Override
	public FloatValue calculate() {
		if(!initialize()) {
			return new FloatValue(0);
		}
		float total = 0f;
		float value = 0f;
		for (int i = 0; i < getTable().getRowCount(); i++) {
			value = intOrFloat(getArgument(), getTable().getRow(i));
			total += value;
		}
		return new FloatValue(total);
	}
}
