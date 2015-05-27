package model.process.analysis.operations.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation of the power function of two NumberValues.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class Power<T extends NumberValue> extends Computation<T> {

	public Power(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Compute left to the power of right.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return new NumberValue with updated value.
	 */
	@Override
	public NumberValue compute(Row row) {
		T left = getLeftSide().resolve(row);
		T right = getRightSide().resolve(row);
		if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue(
					(float) Math.pow((float) left.getValue(), (float) right.getValue()));
		} else if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue(
					(int) Math.pow((int) left.getValue(), (int) right.getValue()));
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
