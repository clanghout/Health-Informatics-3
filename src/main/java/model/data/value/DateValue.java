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
	public DateValue(Integer year, Integer month, Integer day) {
		super(year, month, day, 0, 0, 0);
		if (year == null) {
			year = 0;
		} if (month == null) {
			month = 0;
		} if (day == null) {
			day = 0;
		}
		setSimpleDateFormat("dd-MM-yyyy");
	}

}
