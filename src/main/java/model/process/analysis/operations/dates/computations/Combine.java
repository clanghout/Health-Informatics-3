package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.TemporalValue;
import model.data.value.TimeValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a combination of a date and time into a datetime.
 *
 * Created by Boudewijn on 17-6-2015.
 */
public class Combine extends DateComputation<TimeValue, DateTimeValue> {

	/**
	 * Construct a new Combine.
	 * @param date The describer for the date.
	 * @param time The describer for the time.
	 */
	public Combine(
			DataDescriber<? extends TemporalValue<?>> date,
			DataDescriber<TimeValue> time) {
		super(date, time);
	}

	@Override
	protected DateTimeValue compute(Row row) {
		LocalTime time = getRight().resolve(row).getValue();
		LocalDate date = LocalDate.from(getLeft().resolve(row).getValue());
		return new DateTimeValue(LocalDateTime.of(date, time));
	}
}
