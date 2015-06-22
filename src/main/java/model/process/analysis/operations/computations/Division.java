package model.process.analysis.operations.computations;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation which divides two numberValues.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class Division<T extends NumberValue> extends Computation<T> {

	public Division(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Compute left divided by right.
	 * Always returns FloatValue even if the input is IntValue.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return A new FloatValue with the new value.
	 */
	@Override
	public NumberValue compute(Row row) {
		T left = getLeftSide().resolve(row);
		T right = getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			int rightValue = (int) right.getValue();
			if (rightValue == 0) {
				throw new ArithmeticException("Dividing by zero");
			}
			return new IntValue((int) left.getValue() / rightValue);
		} else if (left instanceof FloatValue && right instanceof FloatValue) {
			float rightValue = (float) right.getValue();
			if (rightValue == 0f) {
				throw new ArithmeticException("Dividing by zero");
			}
			return new FloatValue((float) left.getValue() / rightValue);
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
