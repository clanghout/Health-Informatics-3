package model.process.functions;

import model.data.DataTable;
import model.process.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * A class for calculating the sum of a specified column over all rows.
 * 
 * @author louisgosschalk 16-05-2015
 */
public class Sum extends Function {

	public Sum(DataTable table, DataDescriber<? extends DataValue<?>> argument) {
		super(table, argument);
	}

	/**
	 * This function calculates the sum of a column.
	 */
	@Override
	public NumberValue calculate() {
		if (!initialize()) {
			return new FloatValue(0f);
		}
		float total = 0f;
		float value = 0f;
		for (int i = 0; i < getTable().getRowCount(); i++) {
			value = intOrFloat(getArgument(), getTable().getRow(i));
			total += value;
		}
		if (isInt()) {
			return new IntValue((int) total);
		} 
		return new FloatValue(total);
	}
}
