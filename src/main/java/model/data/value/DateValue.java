package model.data.value;

/**
 * Data Class containing a value with type Date.
 */
public class DateValue extends DateTimeValue {

	/**
	 * Create calendar with zero values for time elements.
	 *
	 * @param year  the year as int
	 * @param month the month as int
	 * @param day   the day as int
	 */
	public DateValue(int year, int month, int day) {
		super(year, month, day, 0, 0, 0);
		setSimpleDateFormat("dd-MM-yyyy");
	}

}
