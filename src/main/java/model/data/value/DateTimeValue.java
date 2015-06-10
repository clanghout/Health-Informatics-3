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
	public DateTimeValue(Integer year, Integer month, Integer day,
			Integer hour, Integer minute, Integer second) {
		super("dd-MM-yyyy HH:mm:ss");
		if (year == null || month == null || day == null || hour == null
				|| minute == null || second == null) {
			setNull(true);
			dateTime = LocalDateTime.of(0, 1, 1, 0, 0, 0);
		} else {
			dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
		}
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

	protected boolean doEquals(Object obj) {
		return ((DateTimeValue) obj).getValue().equals(this.dateTime);
	}
}
