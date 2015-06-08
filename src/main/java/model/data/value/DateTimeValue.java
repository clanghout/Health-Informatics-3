package model.data.value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Data Class containing a value with type Calendar.
 * <p> type of value
 * This is the parent class of the dateValue and TimeValue.
 */
public class DateTimeValue extends DataValue<Calendar> {
	private SimpleDateFormat simpleDateFormat;
	private Calendar value;

	/**
	 * Construct new GregorianCalendar object and simpleDateFormat.
	 *
	 * @param year   the year as int
	 * @param month  the month as int, in the calendar months start by 0 so
	 *               the parameter -1 is used in the creation of the calendar.
	 * @param day    the day as int
	 * @param hour   the hour as int
	 * @param minute the minute as int
	 * @param second the second as int
	 */
	public DateTimeValue(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second) {
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
		this.value = new GregorianCalendar(year, month - 1, day, hour, minute, second);
		setSimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	}

	/**
	 * Set the format of the toString.
	 *
	 * @param format String that specifies the format of the date.
	 */
	public void setSimpleDateFormat(String format) {
		this.simpleDateFormat = new SimpleDateFormat(format);
	}

	@Override
	public Calendar getValue() {
		return (Calendar) value.clone();
	}

	@Override
	public String toString() {
		return simpleDateFormat.format(value.getTime());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DateTimeValue)) {
			return false;
		}
		DateTimeValue other = (DateTimeValue) obj;
		return other.value.equals(this.value)
				&& other.simpleDateFormat.equals(this.simpleDateFormat);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

}
