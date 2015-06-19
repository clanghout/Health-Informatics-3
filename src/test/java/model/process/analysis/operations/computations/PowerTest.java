package model.process.analysis.operations.computations;

import model.process.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the power computation.
 * Created by Chris on 12-5-2015.
 */
public class PowerTest {

	@Test
	public void testPowerIntConstantsZero() throws Exception {
		Power check = new Power<>(new ConstantDescriber<>(new IntValue(0)), new ConstantDescriber<>(new IntValue(0)));
		assertEquals(new IntValue(1), check.compute(null));
	}

	@Test
	public void testPowerIntConstants() throws Exception {
		Power check = new Power<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new IntValue(3)));
		assertEquals(new IntValue(1860867), check.compute(null));
	}

	@Test
	public void testPowerFloatConstantsZero() throws Exception {
		Power check = new Power<>(new ConstantDescriber<>(new FloatValue(0f)), new ConstantDescriber<>(new FloatValue(0f)));
		assertEquals(new FloatValue(1f), check.compute(null));
	}

	@Test
	public void testPowerFloatConstants() throws Exception {
		Power check = new Power<>(new ConstantDescriber<>(new FloatValue(25f)), new ConstantDescriber<>(new FloatValue(2.5f)));
		assertEquals(new FloatValue(3125f), check.compute(null));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIntFloatFail() throws Exception {
		Power check = new Power<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new FloatValue(456.567f)));
		check.compute(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFloatIntFail() throws Exception {
		Power check = new Power<>(new ConstantDescriber<>(new FloatValue(123f)), new ConstantDescriber<>(new IntValue(456)));
		check.compute(null);
	}
}