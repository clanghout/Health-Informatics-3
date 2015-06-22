package model.process.analysis.operations.dates.constraint;

import model.process.describer.ConstantDescriber;
import model.process.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.TimeValue;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 8-6-2015.
 */
public class AfterTest {

	private DataDescriber<DateTimeValue> firstDateTime;
	private DataDescriber<DateTimeValue> secondDateTime;
	private DataDescriber<DateValue> firstDate;
	private DataDescriber<DateValue> secondDate;
	private DataDescriber<TimeValue> firstTime;
	private DataDescriber<TimeValue> secondTime;

	@org.junit.Before
	public void setUp() throws Exception {
		firstDateTime = new ConstantDescriber<>(
				new DateTimeValue(1995, 1, 17, 10, 0, 0)
		);
		secondDateTime = new ConstantDescriber<>(
				new DateTimeValue(1996, 1, 17, 10, 0, 0)
		);

		firstDate = new ConstantDescriber<>(
				new DateValue(1995, 1, 17)
		);

		secondDate = new ConstantDescriber<>(
				new DateValue(1996, 1, 17)
		);

		firstTime = new ConstantDescriber<>(
				new TimeValue(3, 30, 30)
		);

		secondTime = new ConstantDescriber<>(
				new TimeValue(5, 30, 30)
		);
	}

	@Test
	public void testCheckPass() throws Exception {
		After after = new After(firstDateTime, secondDateTime);
		BoolValue resultValue = after.operate(null);
		assertFalse(resultValue.getValue());
	}

	@Test
	public void testCheckFalse() throws Exception {
		After after = new After(secondDateTime, firstDateTime);
		BoolValue resultValue = after.operate(null);
		assertTrue(resultValue.getValue());
	}

	@Test
	public void testDateTimeWithDate() throws Exception {
		After after = new After(firstDateTime, firstDate);
		assertTrue(after.operate(null).getValue());
	}

	@Test
	public void testDateWithDateTime() throws Exception {
		After after = new After(firstDate, firstDateTime);
		assertFalse(after.operate(null).getValue());
	}

	@Test
	public void testTimeWithTime() throws Exception {
		After after = new After(firstTime, secondTime);
		assertFalse(after.operate(null).getValue());
	}

	@Test
	public void testDateWithDate() throws Exception {
		After after = new After(firstDate, secondDate);
		assertFalse(after.operate(null).getValue());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTimeWithDate() throws Exception {
		After after = new After(firstTime, firstDate);
		after.operate(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTimeWithDateTime() throws Exception {
		After after = new After(firstTime, firstDateTime);
		after.operate(null);
	}
}