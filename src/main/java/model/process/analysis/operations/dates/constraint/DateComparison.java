package model.process.analysis.operations.dates.constraint;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.TemporalValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


		long amount = getAmount(leftValue, rightValue);
		return compare(amount);
	}

	private long getAmount(Temporal left, Temporal right) {
		if (left.getClass() == right.getClass()) {
			if (eitherDate(left, right)) {
				return ChronoUnit.DAYS.between(left, right);
			} else {
				return ChronoUnit.SECONDS.between(left, right);
			}
		} else {
			if (eitherDate(left, right)) {
				if (eitherDateTime(left, right)) {
					LocalDateTime leftDateTime = convertToDateTime(left);
					LocalDateTime rightDateTime = convertToDateTime(right);
					return ChronoUnit.SECONDS.between(leftDateTime, rightDateTime);
				}
			}
		}
		throw new UnsupportedOperationException("Time can't be converted to DateTime");
	}

	private boolean eitherDate(Temporal left, Temporal right) {
		return left instanceof LocalDate || right instanceof LocalDate;
	}

	private boolean eitherDateTime(Temporal left, Temporal right) {
		return left instanceof LocalDateTime || right instanceof LocalDateTime;
	}

	private LocalDateTime convertToDateTime(Temporal value) {
		if (value instanceof LocalDateTime) {
			return (LocalDateTime) value;
		} else if (value instanceof LocalDate) {
			return ((LocalDate) value).atStartOfDay();
		} else {
			throw new RuntimeException("This shouldn't happen");
		}
	}

	/**
	 * This method is to be overridden by implementing subclasses to perform the comparison.
	 * @param amount The amount of time between the left and right side operands in seconds.
	 * @return True if the comparison passes, false if not.
	 */
	protected abstract boolean compare(long amount);
}
