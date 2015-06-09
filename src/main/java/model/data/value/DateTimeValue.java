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
<<<<<<< HEAD
	public DateTimeValue(Integer year, Integer month, Integer day,
			Integer hour, Integer minute, Integer second) {
		super("dd-MM-yyyy HH:mm:ss");
		if (year == null || month == null || day == null || hour == null
				|| minute == null || second == null) {
			isNull = true;
			dateTime = LocalDateTime.of(0, 1, 1, 0, 0, 0);
		} else {
			dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
		}
=======
	public DateTimeValue(int year, int month, int day, int hour, int minute, int second) {
		this();
		dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
>>>>>>> master
	}

	public DateTimeValue(Temporal temporal) {
		this();
		dateTime = LocalDateTime.from(temporal);
	}

	@Override
	public LocalDateTime getValue() {
		return dateTime;
	}
}
