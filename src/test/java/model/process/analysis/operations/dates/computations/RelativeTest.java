package model.process.analysis.operations.dates.computations;

import model.process.describer.ConstantDescriber;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.TimeValue;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 9-6-2015.
 */
public class RelativeTest {

	@Test
	public void testDates() throws Exception {
		Relative relative = new Relative(
				new ConstantDescriber<>(
						new DateValue(1995, 1, 17)
				),
				new ConstantDescriber<>(
						new DateValue(1995, 2, 17)
				),
				ChronoUnit.MONTHS
		);

		assertEquals(1, (int) relative.operate(null).getValue());
	}

	@Test
	public void testDateTime() throws Exception {
		Relative relative = new Relative(
				new ConstantDescriber<>(
						new DateTimeValue(1995, 1, 17, 23, 0, 0)
				),
				new ConstantDescriber<>(
						new DateTimeValue(1995, 1, 19, 1, 0, 0)
				),
				ChronoUnit.HOURS
		);

		assertEquals(26, (int) relative.operate(null).getValue());
	}

	@Test
	public void testTime() throws Exception {
		Relative relative = new Relative(
				new ConstantDescriber<>(
						new TimeValue(12, 12, 12)
				),
				new ConstantDescriber<>(
						new TimeValue(11, 12, 12)
				),
				ChronoUnit.HOURS
		);

		assertEquals(-1, (int) relative.operate(null).getValue());
	}
}