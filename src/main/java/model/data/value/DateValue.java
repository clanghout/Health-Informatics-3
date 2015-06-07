package model.data.value;

import java.time.LocalDate;

/**
 * Data Class containing a value with type Date.
 */
public class DateValue extends TemporalValue<LocalDate> {

	private LocalDate date;

	private DateValue() {
		super("dd-MM-yyyy");
	}

	/**
	 * Create calendar with zero values for time elements.
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DateValue dateValue = (DateValue) o;
		return date.equals(dateValue.date);
	}

	@Override
	public int hashCode() {
		return date.hashCode();
	}

	@Override
	public String toString() {
		return getFormatter().format(date);
	}
}
