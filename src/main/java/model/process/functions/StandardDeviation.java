package model.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

/**
 * This function calculates the standard deviation in a column.
 * 
 * @author Louis Gosschalk 19-05-2015
 */
public class StandardDeviation extends Function {

	public StandardDeviation(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
	}

	/**
	 * Calculate average, variance and finally the (standard) deviation.
	 */
	@Override
	public FloatValue calculate() {
		if (!initialize()) {
			return new FloatValue(0f);
		}
		float average = new Average(getTable(), getArgument()).calculate().getValue();
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
		for (int i = 0; i < getTable().getRowCount(); i++) {
			float val = intOrFloat(getArgument(), getTable().getRow(i));
			float dif = val - av;
			dif = dif * dif;
			var += dif;
		}
		return var / getTable().getRowCount();
	}
}