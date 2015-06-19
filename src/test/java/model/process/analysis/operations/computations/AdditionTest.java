package model.process.analysis.operations.computations;

import model.process.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the addition computation.
 * Created by Chris on 12-5-2015.
 */
public class AdditionTest {

	@Test
	public void testAdditionIntConstantsZero() throws Exception {
		Addition check = new Addition<>(new ConstantDescriber<>(new IntValue(0)), new ConstantDescriber<>(new IntValue(0)));
		assertEquals(new IntValue(0), check.compute(null));
	}


	@Test
	public void testAdditionIntConstants() throws Exception {
		Addition check = new Addition<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new IntValue(456)));
		assertEquals(new IntValue(579), check.compute(null));
	}

	@Test
	public void testAdditionFloatConstantsZero() throws Exception {
		Addition check = new Addition<>(new ConstantDescriber<>(new FloatValue(0f)), new ConstantDescriber<>(new FloatValue(0f)));
		assertEquals(new FloatValue(0f), check.compute(null));
	}

	@Test
	public void testAdditionFloatConstants() throws Exception {
		Addition check = new Addition<>(new ConstantDescriber<>(new FloatValue(123f)), new ConstantDescriber<>(new FloatValue(456.567f)));
		assertEquals(new FloatValue(579.567f), check.compute(null));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIntFloatFail() throws Exception {
		Addition check = new Addition<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new FloatValue(456.567f)));
		check.compute(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFloatIntFail() throws Exception {
		Addition check = new Addition<>(new ConstantDescriber<>(new FloatValue(123f)), new ConstantDescriber<>(new IntValue(456)));
		check.compute(null);
	}
}