package model.data.process.analysis.operations.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Compute the square root of a NumberValue.
 */
public class SquareRoot extends UnaryComputation {

	public SquareRoot(DataDescriber leftSide) {
		super(leftSide);
	}

	/**
	 * Computes the square root of left.
	 * @param row The row you want to perform the computation on.
	 * @return new FloatValue containing the computed value.
	 */
	@Override
	public NumberValue compute(DataRow row) {
		NumberValue operand = (NumberValue) getOperand().resolve(row);
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