package model.data.process.analysis.operations.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Created by Chris on 12-5-2015.
 */
public class Substraction extends Computation {

	public Substraction(DataDescriber leftSide, DataDescriber rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public NumberValue compute(DataRow row) {
		NumberValue left = (NumberValue) getLeftSide().resolve(row);
		NumberValue right = (NumberValue) getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue((int) left.getValue() - (int) right.getValue());
		}
		if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue((float) left.getValue() - (float) right.getValue());
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
