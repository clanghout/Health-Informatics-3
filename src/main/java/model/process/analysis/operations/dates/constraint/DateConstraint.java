package model.process.analysis.operations.dates.constraint;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.Operation;

/**
 * Represents a constraint on 2 dates.
 *
 * Created by Boudewijn on 8-6-2015.
 */
public abstract class DateConstraint extends Operation<BoolValue> {

	private DataDescriber<? extends TemporalValue<?>> left;
	private DataDescriber<? extends TemporalValue<?>> right;

	/**
	 * Construct a new DateConstraint.
	 * @param left The left side operand of this constraint.
	 * @param right The right side operand of this constraint.
	 */
	public DateConstraint(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<? extends TemporalValue<?>> right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * This method is to be overridden in implementing subclasses.
	 * @param row The row you want to perform the check on.
	 * @return True if the constraint passed, false if not.
	 */
	protected abstract boolean check(Row row);

	/**
	 * Performs the constraint on the given row.
	 * @param row The row you want to operate on.
	 * @return A BoolValue containing true if the constraint passes, false if not.
	 */
	@Override
	public final BoolValue operate(Row row) {
		return new BoolValue(check(row));
	}

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
	public DataDescriber<? extends TemporalValue<?>> getRight() {
		return right;
	}
}
