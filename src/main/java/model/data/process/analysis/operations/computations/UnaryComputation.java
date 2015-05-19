package model.data.process.analysis.operations.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.process.analysis.operations.Operation;
import model.data.value.NumberValue;

/**
 * Superclass for computations with only one operand.
 *
 * @param <T> The type of the DataDescriber
 */
public abstract class UnaryComputation<T extends NumberValue> extends Operation<NumberValue> {
	private final DataDescriber<T> operand;

	public UnaryComputation(DataDescriber<T> operand) {
		this.operand = operand;
	}

	/**
	 * Perform the computation.
	 * @param row The row you want to perform the computation on.
	 * @return The result of the computation.
	 */
	public abstract NumberValue compute(DataRow row);

	/**
	 * Get the DataDescriber for the left side operand.
	 * @return The DataDescriber for the left side operand
	 */
	public DataDescriber<T> getOperand() {
		return operand;
	}

	@Override
	public NumberValue operate(DataRow row) {
		return compute(row);
	}
}
