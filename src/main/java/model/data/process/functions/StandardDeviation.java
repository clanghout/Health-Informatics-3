package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

/**
 * This function calculates the standard deviation in a column.
 * 
 * @author Louis Gosschalk 19-05-2015
 */
public class StandardDeviation extends Function {

	private DataTable table;
	private DataDescriber<NumberValue> argument;

	public StandardDeviation(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
	}

	/**
	 * Calculate average, variance and finally the (standard) deviation.
	 */
	@Override
	public DataValue calculate() {
		float average = new Average(table, argument).calculate().getValue();
		float variance = variance(average);
		float deviation = (float) Math.sqrt((double) variance);
		FloatValue result = new FloatValue(deviation);
		return result;
	}

	/**
	 * This function calculates the variance in a column.
	 * 
	 * @param av
	 *            float of the average in a column
	 * @return var float of the variance in a column
	 */
	public float variance(float av) {
		float var = 0.0f;
		for (int i = 0; i < table.getRowCount(); i++) {
			float val = intOrFloat(argument, table.getRow(i));
			float dif = val - av;
			dif = dif * dif;
			var += dif;
		}
		return var / table.getRowCount();
	}
}