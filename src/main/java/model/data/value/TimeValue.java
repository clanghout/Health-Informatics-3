package model.data.value;

import java.time.LocalTime;

/**
 * Data Class containing a value with type Time.
 */
public class TimeValue extends TemporalValue<LocalTime> {

	private LocalTime time;

	/**
	 * Construct a new TimeValue.
	 *
	 * @param hour   the hour as Integer
	 * @param minute the minute as Integer
	 * @param second the second as Integer
	 */
	public TimeValue(Integer hour, Integer minute, Integer second) {
		super("HH:mm:ss");
		if (hour == null) {
			hour = 0;
		}
		if (minute == null) {
			minute = 0;
		}
		if (second == null) {
			second = 0;
		}
		time = LocalTime.of(hour, minute, second);
	}

	@Override
	public LocalTime getValue() {
		return time;
	}
}