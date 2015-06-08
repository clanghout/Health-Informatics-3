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
	 * @param hour   the hour as int
	 * @param minute the minute as int
	 * @param second the second as int
	 */
	public TimeValue(int hour, int minute, int second) {
		super("HH:mm:ss");
		time = LocalTime.of(hour, minute, second);
	}

	@Override
	public LocalTime getValue() {
		return time;
	}

	@Override
	public DataValue copy() {
		return this;
	}
}
