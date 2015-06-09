package model.process.analysis.operations.dates.computations;

import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.PeriodValue;
import model.data.value.TemporalValue;
import org.junit.Test;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 8-6-2015.
 */
public class AddTest {

	@Test
	public void testOperate() throws Exception {
		DataDescriber<DateTimeValue> left = new ConstantDescriber<>(
				new DateTimeValue(1995, 1, 17, 10, 0, 0)
		);
		DataDescriber<PeriodValue> right = new ConstantDescriber<>(
				PeriodValue.fromUnit(5, ChronoUnit.DAYS)
		);

		Add add = new Add(left, right);
		TemporalValue<?> temporalValue = add.operate(null);
		Temporal result = temporalValue.getValue();

		assertEquals(1995, result.get(ChronoField.YEAR));
		assertEquals(1, result.get(ChronoField.MONTH_OF_YEAR));
		assertEquals(22, result.get(ChronoField.DAY_OF_MONTH));
		assertEquals(10, result.get(ChronoField.HOUR_OF_DAY));
		assertEquals(0, result.get(ChronoField.MINUTE_OF_HOUR));
		assertEquals(0, result.get(ChronoField.SECOND_OF_MINUTE));
	}
}