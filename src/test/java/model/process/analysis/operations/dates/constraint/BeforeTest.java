package model.process.analysis.operations.dates.constraint;

import model.process.describer.ConstantDescriber;
import model.process.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DateTimeValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 8-6-2015.
 */
public class BeforeTest {

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
		Before before = new Before(first, second);
		BoolValue resultValue = before.operate(null);
		assertTrue(resultValue.getValue());
	}

	@Test
	public void testCheckFalse() throws Exception {
		Before before = new Before(second, first);
		BoolValue resultValue = before.operate(null);
		assertFalse(resultValue.getValue());
	}

}