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
		if (hour == null || minute == null || second == null) {
			time = LocalTime.of(0, 0, 0);
			setNull(true);
		} else {
			time = LocalTime.of(hour, minute, second);
		}
	}

	@Override
	public LocalTime getValue() {
		return time;
	}

	@Override
	protected boolean doEquals(Object obj) {
		return ((TimeValue) obj).getValue().equals(this.time);
	}
}