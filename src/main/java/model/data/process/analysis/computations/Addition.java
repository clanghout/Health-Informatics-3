package model.data.process.analysis.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation wich adds two numbervalues.
 */
public class Addition extends Computation {

	public Addition(DataDescriber leftSide, DataDescriber rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public NumberValue compute(DataRow row) throws Exception {
		NumberValue left = (NumberValue) getLeftSide().resolve(row);
		NumberValue right = (NumberValue) getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue((int) left.getValue() + (int) right.getValue());
		}
		if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue((float) left.getValue() + (float) right.getValue());
		} else {
			throw new Exception("Function for this type is not supported.");
		}
	}
}
