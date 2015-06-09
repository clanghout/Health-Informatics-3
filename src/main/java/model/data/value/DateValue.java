package model.data.value;

import java.time.LocalDate;
import java.time.temporal.Temporal;

/**
 * Represent a value containg a date.
 */
public class DateValue extends TemporalValue<LocalDate> {

	private LocalDate date;

	private DateValue() {
		super("dd-MM-yyyy");
	}

	/**
	 * Construct a new DateValue.
	 *
	 * @param year  the year as int
	 * @param month the month as int
	 * @param day   the day as int
	 */
	public DateValue(int year, int month, int day) {
		this();
		date = LocalDate.of(year, month, day);
	}

	public DateValue(Temporal date) {
		this();
		this.date = LocalDate.from(date);
	}

	@Override
	public LocalDate getValue() {
		return date;
	}
}
