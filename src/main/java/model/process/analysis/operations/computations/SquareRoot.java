package model.process.analysis.operations.computations;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Compute the square root of a NumberValue.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public class SquareRoot<T extends NumberValue> extends UnaryComputation<T> {

	public SquareRoot(DataDescriber<T> operand) {
		super(operand);
	}

	/**
	 * Computes the square root of left.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return new FloatValue containing the computed value.
	 */
	@Override
	public NumberValue compute(Row row) {
		T operand = getOperand().resolve(row);
		if (operand instanceof FloatValue) {
			float value = (float) operand.getValue();
			if (value < 0f) {
				throw new ArithmeticException("Function not defined for this value.");
			}
			return new FloatValue((float) Math.sqrt(value));
		} else if (operand instanceof IntValue) {
			int value = (int) operand.getValue();
			if (value < 0) {
				throw new ArithmeticException("Function not defined for this value.");
			}
			return new FloatValue((float) Math.sqrt(value));
		} else {
			throw new UnsupportedOperationException("Function for this type is not supported.");
		}
	}
}