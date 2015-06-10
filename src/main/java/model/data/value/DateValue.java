package model.data.value;

import java.time.LocalDate;
import java.time.temporal.Temporal;

/**
 * Represent a value containing a date.
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

	public DateValue(Temporal date) {
		this();
		this.date = LocalDate.from(date);
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
	
	protected boolean doEquals(Object obj) {
		return ((DateValue) obj).getValue().equals(this.date);
	}
}
