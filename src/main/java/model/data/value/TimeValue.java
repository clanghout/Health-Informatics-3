package model.data.value;

/**
 * Data Class containing a value with type Time.
 */
public class TimeValue extends DateTimeValue {
	/**
	 * Create Calendar with zero values for date elements and set DateFormat time only.
	 * The super call has month value 1 because the super constructor loweres the month value by 1
	 * while creating the calendar.
	 *
	 * @param hour   the hour as int
	 * @param minute the minute as int
	 * @param second the second as int
	 */
	public TimeValue(int hour, int minute, int second) {
		super(0, 1, 0, hour, minute, second);
		setSimpleDateFormat("HH:mm:ss");
	}
}
