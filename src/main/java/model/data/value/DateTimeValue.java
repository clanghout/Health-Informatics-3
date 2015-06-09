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
	}

	@Override
	public LocalDateTime getValue() {
		return dateTime;
	}
}
