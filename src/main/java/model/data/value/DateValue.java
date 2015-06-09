package model.data.value;

import java.time.LocalDate;

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

	public DateValue(LocalDate date) {
		this();
		this.date = date;
	}

	@Override
	public LocalDate getValue() {
		return date;
	}

	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof DateValue)) {
			throw new IllegalArgumentException("Cannot compare non datevalue to datevalue.");
		}
		DateValue o = (DateValue) other;
		return date.compareTo(o.date);
	}
}
