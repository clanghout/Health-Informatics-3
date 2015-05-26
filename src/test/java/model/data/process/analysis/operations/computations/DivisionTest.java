package model.data.process.analysis.operations.computations;

import model.data.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the division computation.
 * Created by Chris on 12-5-2015.
 */
public class DivisionTest {

	@Test
	public void testDivisionIntConstantsZero() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new IntValue(0)), new ConstantDescriber<>(new IntValue(1)));
		assertEquals(new IntValue(0), check.compute(null));
	}

	@Test
	public void testDivisionIntConstants() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new IntValue(12300)), new ConstantDescriber<>(new IntValue(20)));
		assertEquals(new IntValue(615), check.compute(null));
	}

	@Test
	public void testDivisionFloatConstantsZero() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new FloatValue(0f)), new ConstantDescriber<>(new FloatValue(1f)));
		assertEquals(new FloatValue(0f), check.compute(null));
	}

	@Test
	public void testDivisionFloatConstants() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new FloatValue(12.5f)), new ConstantDescriber<>(new FloatValue(4)));
		assertEquals(new FloatValue(3.125f), check.compute(null));
	}

	@Test(expected = ArithmeticException.class)
	public void testDivideZeroInt() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new IntValue(8756)), new ConstantDescriber<>(new IntValue(0)));
		check.compute(null);
	}

	@Test(expected = ArithmeticException.class)
	public void testDivideZeroFloat() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new FloatValue(123f)), new ConstantDescriber<>(new FloatValue(0)));
		check.compute(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIntFloatFail() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new FloatValue(456.567f)));
		check.compute(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFloatIntFail() throws Exception {
		Division check = new Division<>(new ConstantDescriber<>(new FloatValue(123)), new ConstantDescriber<>(new IntValue(456)));
		check.compute(null);
	}
}