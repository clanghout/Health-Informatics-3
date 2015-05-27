package model.process.analysis.operations.computations;

import model.data.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the multiplication computation.
 * Created by Chris on 12-5-2015.
 */
public class MultiplicationTest {

	@Test
	public void testMultiplicationIntConstantsZero() throws Exception {
		Multiplication check = new Multiplication<>(new ConstantDescriber<>(new IntValue(0)), new ConstantDescriber<>(new IntValue(1)));
		assertEquals(new IntValue(0), check.compute(null));
	}

	@Test
	public void testMultiplicationIntConstants() throws Exception {
		Multiplication check = new Multiplication<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new IntValue(20)));
		assertEquals(new IntValue(2460), check.compute(null));
	}

	@Test
	public void testMultiplicationFloatConstantsZero() throws Exception {
		Multiplication check = new Multiplication<>(new ConstantDescriber<>(new FloatValue(0f)), new ConstantDescriber<>(new FloatValue(1f)));
		assertEquals(new FloatValue(0f), check.compute(null));
	}

	@Test
	public void testMultiplicationFloatConstants() throws Exception {
		Multiplication check = new Multiplication<>(new ConstantDescriber<>(new FloatValue(125f)), new ConstantDescriber<>(new FloatValue(12.5f)));
		assertEquals(new FloatValue(1562.5f), check.compute(null));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIntFloatFail() throws Exception {
		Multiplication check = new Multiplication<>(new ConstantDescriber<>(new IntValue(123)), new ConstantDescriber<>(new FloatValue(456.567f)));
		check.compute(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFloatIntFail() throws Exception {
		Multiplication check = new Multiplication<>(new ConstantDescriber<>(new FloatValue(123)), new ConstantDescriber<>(new IntValue(456)));
		check.compute(null);
	}
}