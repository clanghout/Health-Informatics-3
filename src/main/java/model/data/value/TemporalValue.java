package model.data.value;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * This class provides a basis for classes dealing with Date or Time values.
 *
 * Created by Boudewijn on 7-6-2015.
 * @param <T> The type of value contained in this DataValue.
 */
abstract class TemporalValue<T extends TemporalAccessor> extends DataValue<T> {

	private String format;

	TemporalValue(String format) {
		if (format == null) {
			this.format = "";
			isNull = true;
		} else {
			this.format = format;
		}
	}

	/**
	 * Get the format of this TemporalValue.
	 * @return The pattern used to format the time in this value.
	 */
	public String getFormat() {
		return format;
	}

	DateTimeFormatter getFormatter() {
		return DateTimeFormatter.ofPattern(getFormat());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		TemporalValue<?> otherValue = (TemporalValue<?>) obj;
		return this.getValue().equals(otherValue.getValue());
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public String toString() {
		return getFormatter().format(getValue());
	}
}
