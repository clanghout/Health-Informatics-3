package model.process.analysis.operations.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Computation which modulates one NumberValue by another.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class Modulo<T extends NumberValue> extends Computation<T> {

	/**
	 * Construct a new Modulo.
	 * @param leftSide The left side operand.
	 * @param rightSide The right side operand.
	 */
	public Modulo(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Compute left modulated by right.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return new NumberValue with updated value.
	 */
	@Override
	public NumberValue compute(Row row) {
		T left = getLeftSide().resolve(row);
		T right = getRightSide().resolve(row);
		if (left instanceof IntValue && right instanceof IntValue) {
			return new IntValue((int) left.getValue() % (int) right.getValue());
		} else if (left instanceof FloatValue && right instanceof FloatValue) {
			return new FloatValue((float) left.getValue() % (float) right.getValue());
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}
