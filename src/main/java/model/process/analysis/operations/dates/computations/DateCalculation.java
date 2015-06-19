package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.PeriodValue;
import model.data.value.TemporalValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Represents a calculation performed on a date.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public abstract class DateCalculation extends DateComputation<PeriodValue, TemporalValue<?>> {


	/**
	 * Construct a new DateCalculation.
	 * @param left The left side operand of this operation.
	 * @param right The right side operand of this operation.
	 */
	public DateCalculation(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<PeriodValue> right) {
		super(left, right);
	}

	protected final TemporalValue<?> compute(Row row) {
		Temporal moment = calculate(row);
		if (moment instanceof LocalDateTime) {
			return new DateTimeValue(moment);
		} else if (moment instanceof LocalDate) {
			return new DateValue(moment);
		} else {
			throw new UnsupportedOperationException(
					String.format("Type of %s not recognized", moment)
			);
		}
	}

	/**
	 * This method is to be overridden by implementing subclasses to perform the date calculation.
	 * @param row The row on which you want to perform the calculation on.
	 * @return The result of the calculation.
	 */
	protected abstract Temporal calculate(Row row);
}
