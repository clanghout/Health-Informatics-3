package model.data.value;

import java.time.format.DateTimeFormatter;

/**
 * This class provides a basis for classes dealing with Date or Time values.
 *
 * Created by Boudewijn on 7-6-2015.
 * @param <T> The type of value contained in this DataValue.
 */
abstract class TemporalValue<T> extends DataValue<T> {

	private String format;

	TemporalValue(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	DateTimeFormatter getFormatter() {
		return DateTimeFormatter.ofPattern(getFormat());
	}
}
