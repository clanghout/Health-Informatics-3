package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;

/**
 * Extracts the date part of a date time.
 *
 * Created by Boudewijn on 17-6-2015.
 */
public class ExtractDate extends DateComputation<DateTimeValue, DateValue> {

	/**
	 * Construct a new ExtractDate.
	 * @param dateTimeValue The date time value to be extracted from.
	 */
	public ExtractDate(DataDescriber<DateTimeValue> dateTimeValue) {
		super(null, dateTimeValue);
	}

	@Override
	protected DateValue compute(Row row) {
		return new DateValue(getRight().resolve(row).getValue().toLocalTime());
	}
}
