package model.data.value;

/**
 * Data Class containing a value with type Time.
 */
public class TimeValue extends DateTimeValue {
	/**
	 * Create Calendar with zero values for date elements and set DateFormat
	 * time only. The super call has month value 1 because the super constructor
	 * loweres the month value by 1 while creating the calendar.
	 *
	 * @param hour
	 *            the hour as Integer
	 * @param minute
	 *            the minute as Integer
	 * @param second
	 *            the second as Integer
	 */
	public TimeValue(Integer hour, Integer minute, Integer second) {
		super(0, 1, 0, hour, minute, second);
		if (hour == null) {
			hour = 0;
		}
		if (minute == null) {
			minute = 0;
		}
		if (second == null) {
			second = 0;
		}
		setSimpleDateFormat("HH:mm:ss");
	}
}
