package model.process.describer;

import model.data.Row;
import model.process.analysis.operations.computations.Computation;
import model.data.value.NumberValue;


/**
 * A datadescriber for the output of a computation.
 *
 * @param <T> Type of NumberValue being used by the computation.
 */
public final class ComputationDescriber<T extends NumberValue> extends DataDescriber<T> {
	private Computation computation;

	/**
	 * Construct a new ConstraintDescriber.
	 *
	 * @param computation The constraint this describer is describing.
	 */
	public ComputationDescriber(Computation computation) {
		this.computation = computation;
	}

	/**
	 * Resolves the value of this constraint.
	 *
	 * @param row The row the value should be resolved from
	 * @return A BoolValue containing the result of the constraint on the row
	 */
	@Override
	public T resolve(Row row) {
		return (T) computation.compute(row);
	}
}
