package model.data.value;

import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;

/**
 * Data Class containing a value with type Time.
 */
public class TimeValue extends TemporalValue<LocalTime> {

	private LocalTime time;

	/**
	 * Return a null instance.
	 */
	TimeValue() {
		this(null, null, null);
	}

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

	/**
	 * Construct a new TimeValue from a TemporalAccessor.
	 * @param time The time this value should be.
	 */
	public TimeValue(TemporalAccessor time) {
		super("HH:mm:ss");
		this.time = LocalTime.from(time);
	}

	@Override
	public LocalTime getValue() {
		return time;
	}
	
	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof TimeValue)) {
			throw new IllegalArgumentException("Cannot compare Time with non Time.");
		}
		TimeValue o = (TimeValue) other;
		return time.compareTo(o.time);
	}

	@Override
	protected boolean doEquals(Object obj) {
		return ((TimeValue) obj).getValue().equals(this.time);
	}
}
