package model.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Function used to count the number of rows in the result.
 * Created by jens on 6/4/15.
 */
public class Count extends Function {

	public Count(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
	}

	/**
	 * Return the amount of rows in the table.
	 * @return number of rows in this table
	 */
	@Override
	public IntValue calculate() {
		return new IntValue(getTable().getRowCount());
	}
}
