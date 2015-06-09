package model.data.value;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Represents a period in dates. Time is not supported.
 *
 * Created by Boudewijn on 7-6-2015.
 */
public class PeriodValue extends DataValue<Period> {

	private Period period;

	public PeriodValue(Integer years, Integer months, Integer days) {
		if (years == null || months == null || days == null) {
			period = Period.of(0, 0, 0);
			setNull(true);
		} else {
			period = Period.of(years, months, days);
		}
	}

	public static PeriodValue fromUnit(int amount, TemporalUnit unit) {
		if (unit == ChronoUnit.DAYS) {
			return new PeriodValue(0, 0, amount);
		} else if (unit == ChronoUnit.MONTHS) {
			return new PeriodValue(0, amount, 0);
		} else if (unit == ChronoUnit.YEARS) {
			return new PeriodValue(amount, 0, 0);
		} else {
			throw new IllegalArgumentException(String.format("Unit %s isn't supported by "
					+ "PeriodValue, only "
					+ "days, months and years are", unit));
		}
	}

	@Override
	public Period getValue() {
		return period;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PeriodValue that = (PeriodValue) o;
		return period.equals(that.period);
	}

	@Override
	public int hashCode() {
		return period.hashCode();
	}

	@Override
	public String toString() {
		return period.toString();
	}
}
