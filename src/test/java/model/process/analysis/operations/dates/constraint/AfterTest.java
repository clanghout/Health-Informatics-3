package model.process.analysis.operations.dates.constraint;

import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DateTimeValue;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 8-6-2015.
 */
public class AfterTest {

	private DataDescriber<DateTimeValue> first;
	private DataDescriber<DateTimeValue> second;

	@org.junit.Before
	public void setUp() throws Exception {
		first = new ConstantDescriber<>(
				new DateTimeValue(1995, 1, 17, 10, 0, 0)
		);
		second = new ConstantDescriber<>(
				new DateTimeValue(1996, 1, 17, 10, 0, 0)
		);
	}

	@Test
	public void testCheckPass() throws Exception {
		After after = new After(first, second);
		BoolValue resultValue = after.operate(null);
		assertFalse(resultValue.getValue());
	}

	@Test
	public void testCheckFalse() throws Exception {
		After after = new After(second, first);
		BoolValue resultValue = after.operate(null);
		assertTrue(resultValue.getValue());
	}
}