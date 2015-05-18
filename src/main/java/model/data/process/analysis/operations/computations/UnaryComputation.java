package model.data.process.analysis.operations.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.process.analysis.operations.Operation;
import model.data.value.NumberValue;

/**
 * Abstract class describing computations on data.
 *
 * @param <T> The type of the left and right side DataDescribers
 */
public abstract class UnaryComputation<T extends NumberValue> extends Operation<NumberValue> {
	private final DataDescriber<T> left;

	public UnaryComputation(DataDescriber<T> leftSide) {
		left = leftSide;
	}

	/**
	 * Perform the computation.
	 *
	 * @param row The row you want to perform the computation on.
	 * @return The result of the computation.
	 */
	public abstract NumberValue compute(DataRow row);

	/**
	 * Get the DataDescriber for the left side operand.
	 *
	 * @return The DataDescriber for the left side operand
	 */
	public DataDescriber<T> getLeftSide() {
		return left;
	}

	@Override
	public NumberValue operate(DataRow row) {
		return compute(row);
	}
}
