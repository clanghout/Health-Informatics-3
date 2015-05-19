package model.data.process.analysis.operations.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation which multiplies two NumberValues.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class Multiplication<T extends NumberValue> extends Computation<T> {

	public Multiplication(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Compute left multiplied by right.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return new NumberValue with updated value.
	 */
	@Override
	public NumberValue compute(DataRow row) {
		NumberValue left = (NumberValue) getLeftSide().resolve(row);
		NumberValue right = (NumberValue) getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue((int) left.getValue() * (int) right.getValue());
		} else if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue((float) left.getValue() * (float) right.getValue());
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
