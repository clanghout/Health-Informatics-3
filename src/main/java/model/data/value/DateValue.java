package model.data.value;

/**
 * Data Class containing a value with type Date.
 */
public class DateValue extends DateTimeValue {

	public DateValue(int year, int month, int day) {
		super(year, month, day, 0, 0, 0);
		setSimpleDateFormat("dd-MM-yyyy");
	}

}
