package model.data.process.analysis.operations.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation which adds two NumberValues.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class Addition<T extends NumberValue> extends Computation<T> {

	public Addition(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Compute left added to right.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return new NumberValue with updated value.
	 */
	@Override
	public NumberValue compute(DataRow row) {
		T left = getLeftSide().resolve(row);
		T right = getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue((int) left.getValue() + (int) right.getValue());
		} else if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue((float) left.getValue() + (float) right.getValue());
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
