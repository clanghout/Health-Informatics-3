package model.data.value;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Represents a DateTime value.
 */
public class DateTimeValue extends TemporalValue<LocalDateTime> {

	private LocalDateTime dateTime;

	private DateTimeValue() {
		super("dd-MM-yyyy HH:mm:ss");
	}

	/**
	 * Construct new DateTimeValue.
	 */
	public DateTimeValue(int year, int month, int day, int hour, int minute, int second) {
		this();
		dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
	}

	public DateTimeValue(Temporal temporal) {
		this();
		dateTime = LocalDateTime.from(temporal);
	}

	@Override
	public LocalDateTime getValue() {
		return dateTime;
	}

	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof DateTimeValue)) {
			throw new IllegalArgumentException("Cannot compare DataTime with non DateTime.");
		}
		DateTimeValue o = (DateTimeValue) other;
		return dateTime.compareTo(o.dateTime);
	}
}
