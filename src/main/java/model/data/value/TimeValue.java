package model.data.value;

/**
 * Data Class containing a value with type Time.
 */
public class TimeValue extends DateTimeValue {
	public TimeValue(int hour, int minute, int second) {
		super(0, 1, 0, hour, minute, second);
		setSimpleDateFormat("HH:mm:ss");
	}
}
