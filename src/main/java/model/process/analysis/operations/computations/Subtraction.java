package model.process.analysis.operations.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation which subtracts two NumberValues.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class Subtraction<T extends NumberValue> extends Computation<T> {

	public Subtraction(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public NumberValue compute(Row row) {
		T left = getLeftSide().resolve(row);
		T right = getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue((int) left.getValue() - (int) right.getValue());
		} else if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue((float) left.getValue() - (float) right.getValue());
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
