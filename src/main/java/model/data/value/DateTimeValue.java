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
	public DateTimeValue(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second) {
		super("dd-MM-yyyy HH:mm:ss");
		if (year == null) {
			year = 0;
			month = 1;
			day = 1;
			hour = 0;
			minute = 0;
			second = 0;
		}
		dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
	}

	@Override
	public LocalDateTime getValue() {
		return dateTime;
	}
}

