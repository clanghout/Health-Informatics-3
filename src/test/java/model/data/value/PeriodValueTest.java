package model.data.value;

import org.junit.Test;

import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 7-6-2015.
 */
public class PeriodValueTest {

	@Test
	public void testFromUnitDays() throws Exception {
		PeriodValue value = PeriodValue.fromUnit(5, ChronoUnit.DAYS);
		PeriodValue days = new PeriodValue(0, 0, 5);
		assertEquals(days, value);
		assertEquals(days.hashCode(), value.hashCode());
	}

	@Test
	public void testFromUnitMonths() throws Exception {
		PeriodValue value = PeriodValue.fromUnit(5, ChronoUnit.MONTHS);
		PeriodValue months = new PeriodValue(0, 5, 0);
		assertEquals(months, value);
		assertEquals(months.hashCode(), value.hashCode());
	}

	@Test
	public void testFromUnitYears() throws Exception {
		PeriodValue value = PeriodValue.fromUnit(5, ChronoUnit.YEARS);
		PeriodValue years = new PeriodValue(5, 0, 0);
		assertEquals(years, value);
		assertEquals(years.hashCode(), value.hashCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromIncorrectUnit() throws Exception {
		PeriodValue.fromUnit(5, ChronoUnit.SECONDS);
	}

	@Test
	public void testGetValue() throws Exception {
		PeriodValue periodValue = new PeriodValue(5, 4, 3);
		Period value = periodValue.getValue();
		Period expected = Period.of(5, 4, 3);
		assertEquals(expected, value);
	}
}