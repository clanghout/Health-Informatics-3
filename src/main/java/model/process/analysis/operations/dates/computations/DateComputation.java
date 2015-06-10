package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.Operation;

/**
 * Represents a computation performed on a temporal value.
 *
 * Created by Boudewijn on 8-6-2015.
 * @param <T> The type of the right side operand.
 * @param <R> The result of the computation.
 */
public abstract class DateComputation<T extends DataValue<?>, R extends DataValue<?>>
		extends Operation<R> {

	private DataDescriber<? extends TemporalValue<?>> left;
	private DataDescriber<T> right;

	/**
	 * Construct a new DateComputation.
	 * @param left The left side operand of this operation.
	 * @param right The right side operand of this operation.
	 */
	public DateComputation(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<T> right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Performs the operation.
	 * @param row The row you want to operate on.
	 * @return The result of this operation.
	 */
	@Override
	public final R operate(Row row) {
		return compute(row);
	}

	/**
	 * This method is to be overridden by implementing subclasses to perform the date computation.
	 * @param row The row on which you want to perform the computation on.
	 * @return The result of the computation.
	 */
	protected abstract R compute(Row row);

	/**
	 * Get the left side operand of this operation.
	 * @return The left side operand of this operation.
	 */
	public DataDescriber<? extends TemporalValue<?>> getLeft() {
		return left;
	}

	/**
	 * Get the right side operand of this operation.
	 * @return The right side operand of this operation.
	 */
	public DataDescriber<T> getRight() {
		return right;
	}
}
