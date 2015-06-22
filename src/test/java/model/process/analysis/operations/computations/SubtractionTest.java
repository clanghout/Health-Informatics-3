package model.process.analysis.operations.computations;

import model.process.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the subtraction computation.
 * Created by Chris on 12-5-2015.
 */
public class SubtractionTest {

	@Test
	public void testSubtractionIntConstantsZero() throws Exception {
		Subtraction check = new Subtraction<>(new ConstantDescriber<>(new IntValue(0)), new ConstantDescriber<>(new IntValue(0)));
		assertEquals(new IntValue(0), check.compute(null));
	}

	@Test
	public void testSubtractionIntConstants() throws Exception {
		Subtraction check = new Subtraction<>(new ConstantDescriber<>(new IntValue(456)), new ConstantDescriber<>(new IntValue(123)));
		assertEquals(new IntValue(333), check.compute(null));
		check = new Subtraction<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new IntValue(456)));
		assertEquals(new IntValue(-333), check.compute(null));
	}

	@Test
	public void testSubtractionFloatConstantsZero() throws Exception {
		Subtraction check = new Subtraction<>(new ConstantDescriber<>(new FloatValue(0f)), new ConstantDescriber<>(new FloatValue(0f)));
		assertEquals(new FloatValue(0f), check.compute(null));
	}

	@Test
	public void testSubtractionFloatConstants() throws Exception {
		Subtraction check = new Subtraction<>(new ConstantDescriber<>(new FloatValue(1f)), new ConstantDescriber<>(new FloatValue(0.125f)));
		assertEquals(new FloatValue(0.875f), check.compute(null));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIntFloatFail() throws Exception {
		Subtraction check = new Subtraction<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new FloatValue(456.567f)));
		check.compute(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFloatIntFail() throws Exception {
		Subtraction check = new Subtraction<>(new ConstantDescriber<>(new FloatValue(123f)), new ConstantDescriber<>(new IntValue(456)));
		check.compute(null);
	}
}