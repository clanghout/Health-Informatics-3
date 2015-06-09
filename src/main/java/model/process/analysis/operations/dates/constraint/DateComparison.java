package model.process.analysis.operations.dates.constraint;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.TemporalValue;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * Respresents a comparison between 2 Dates.
 *
 * Created by Boudewijn on 8-6-2015.
 */
public abstract class DateComparison extends DateConstraint {

	/**
	 * Construct a new DateComparison.
	 * @param left The left side operand of this comparison.
	 * @param right The right side operand of this comparison.
	 */
	public DateComparison(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<? extends TemporalValue<?>> right) {
		super(left, right);
	}

	/**
	 * Performs the comparison.
	 * @param row The row you want to perform the check on.
	 * @return True if the comparison passes, false if not.
	 */
	@Override
	protected boolean check(Row row) {
		Temporal leftValue = getLeft().resolve(row).getValue();
		Temporal rightValue = getRight().resolve(row).getValue();
		long amount = ChronoUnit.SECONDS.between(leftValue, rightValue);
		return compare(amount);
	}

	/**
	 * This method is to be overridden by implementing subclasses to perform the comparison.
	 * @param amount The amount of time between the left and right side operands in seconds.
	 * @return True if the comparison passes, false if not.
	 */
	protected abstract boolean compare(long amount);
}
