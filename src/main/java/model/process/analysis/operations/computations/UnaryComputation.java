package model.process.analysis.operations.computations;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * Superclass for computations with only one operand.
 *
 * @param <T> The type of the DataDescriber
 */
public abstract class UnaryComputation<T extends NumberValue> extends Computation<T> {
	private final DataDescriber<T> operand;

	public UnaryComputation(DataDescriber<T> operand) {
		super(operand, null);
		this.operand = operand;
	}

	/**
	 * Perform the computation.
	 * @param row The row you want to perform the computation on.
	 * @return The result of the computation.
	 */
	public abstract NumberValue compute(Row row);

	/**
	 * Get the DataDescriber for the operand.
	 * @return The DataDescriber for the operand
	 */
	public DataDescriber<T> getOperand() {
		return operand;
	}

	@Override
	public NumberValue operate(Row row) {
		return compute(row);
	}
}
