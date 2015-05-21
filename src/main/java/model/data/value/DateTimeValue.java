package model.data.value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Data Class containing a value with type Time.
 */
public class DateTimeValue extends DataValue<Calendar> {
	private SimpleDateFormat simpleDateFormat;
	private Calendar value;

	public DateTimeValue(int year, int month, int day, int hour, int minute, int second) {
		this.value = new GregorianCalendar(year, month - 1, day, hour, minute, second);
		setSimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	}

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
