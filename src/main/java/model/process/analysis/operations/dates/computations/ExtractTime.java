package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.TimeValue;

/**
 * Extracts the time part of a date time.
 *
 * Created by Boudewijn on 17-6-2015.
 */
public class ExtractTime extends DateComputation<DateTimeValue, TimeValue> {

	/**
	 * Construct a new ExtractTime.
	 * @param dateTimeValue The date time value to be extracted from.
	 */
	public ExtractTime(DataDescriber<DateTimeValue> dateTimeValue) {
		super(null, dateTimeValue);
	}

	@Override
	protected TimeValue compute(Row row) {
		return new TimeValue(getRight().resolve(row).getValue().toLocalTime());
	}
}
