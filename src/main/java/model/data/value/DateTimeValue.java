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
		} if (month == null) {
			month = 0;
		} if (day == null) {
			day = 0;
		} if (hour == null) {
			hour = 0;
		} if (minute == null) {
			minute = 0;
		} if (second == null) {
			second = 0;
		}
		dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
	}

	@Override
	public LocalDateTime getValue() {
		return dateTime;
	}
}

