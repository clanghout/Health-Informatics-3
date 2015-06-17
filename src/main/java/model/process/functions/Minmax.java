package model.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * This class will provide a framework for minimum and maximum.
 * 
 * @author louisgosschalk 13-05-2015
 */
abstract class Minmax extends Function {

	Minmax(DataTable table, DataDescriber<NumberValue> argument) {
		super(table, argument);
	}

	/**
	 * This function checks restrictions for determining minimum & maximum.
	 * 
	 * @return List<DataRow> the rows containing the minimum
	 */
	@Override
	public NumberValue calculate() {
		if (!initialize()) {
			return new FloatValue(0f);
		}
		return compare();
	}

	/**
	 * This function calculates minimum or maximum through a generic arithmetic calculation.
	 * 
	 * @return List<DataRow> a list of DataRows
	 */
	public NumberValue compare() {
		float current = 0.0f;
		current = intOrFloat(getArgument(), getTable().getRow(0));
		for (int i = 1; i < getTable().getRowCount(); i++) {
			float compare = 0.0f;
			compare = intOrFloat(getArgument(), getTable().getRow(i));
			float comparison = current - compare;
			if (check(comparison)) {
				current = compare;
			}
		}
		if (isInt()) {
			return new IntValue((int) current);
		}
		return new FloatValue(current);
	}

	public abstract boolean check(float comparison);
}
