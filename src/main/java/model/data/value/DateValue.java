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
	 * @param year
	 *            the year as int
	 * @param month
	 *            the month as int
	 * @param day
	 *            the day as int
	 */
	public DateValue(Integer year, Integer month, Integer day) {
		this();
		if (year == null || month == null || day == null) {
			date = LocalDate.of(0, 1, 1);
			setNull(true);
		} else {
			date = LocalDate.of(year, month, day);
		}
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
	protected boolean doEquals(Object obj) {
		return ((DateValue) obj).getValue() == this.date;
	}
}