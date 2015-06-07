package model.data.value;

import java.time.LocalDateTime;

/**
 * Represents a DateTime value.
 */
public class DateTimeValue extends TemporalValue<LocalDateTime> {

	private LocalDateTime dateTime;

	/**
	 * Construct new DateTimeValue.
	 */
	public DateTimeValue(int year, int month, int day, int hour, int minute, int second) {
		super("dd-MM-yyyy HH:mm:ss");
		dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
	}

	@Override
	public LocalDateTime getValue() {
		return dateTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DateTimeValue that = (DateTimeValue) o;

		return dateTime.equals(that.dateTime);

	}

	@Override
	public int hashCode() {
		return dateTime.hashCode();
	}

	@Override
	public DataValue copy() {
		return this;
	}

	@Override
	public String toString() {
		return getFormatter().format(dateTime);
	}
}
