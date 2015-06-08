package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.PeriodValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.Operation;

import java.time.temporal.Temporal;

/**
 * Represents a computation performed on a temporal value.
 *
 * Created by Boudewijn on 8-6-2015.
 */
public abstract class DateComputation extends Operation<TemporalValue<?>> {

	private DataDescriber<? extends TemporalValue<?>> left;
	private DataDescriber<PeriodValue> right;

	/**
	 * Construct a new DateComputation.
	 * @param left The left side operand of this operation.
	 * @param right The right side operand of this operation.
	 */
	public DateComputation(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<PeriodValue> right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Performs the operation.
	 * @param row The row you want to operate on.
	 * @return The result of this operation.
	 */
	@Override
	public final TemporalValue operate(Row row) {
		return new DateTimeValue(compute(row));
	}

	/**
	 * This method is to be overridden by implementing subclasses to perform the date computation.
	 * @param row The row on which you want to perform the computation on.
	 * @return The result of the computation.
	 */
	protected abstract Temporal compute(Row row);

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
	public DataDescriber<PeriodValue> getRight() {
		return right;
	}
}
