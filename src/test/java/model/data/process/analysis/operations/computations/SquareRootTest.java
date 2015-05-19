package model.data.process.analysis.operations.computations;

import model.data.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the square root computation.
 * Created by Chris on 12-5-2015.
 */
public class SquareRootTest {

	@Test
	public void testSquareRootZero() throws Exception {
		SquareRoot check = new SquareRoot(new ConstantDescriber<>(new IntValue(0)));
		assertEquals(new FloatValue(0), check.compute(null));
	}

	@Test
	public void testSquareRootConstant() throws Exception {
		SquareRoot check = new SquareRoot(new ConstantDescriber<>(new IntValue(144)));
		assertEquals(new FloatValue(12), check.compute(null));
	}

	@Test
	public void testSquareRootFloatZero() throws Exception {
		SquareRoot check = new SquareRoot(new ConstantDescriber<>(new FloatValue(0.0f)));
		assertEquals(new FloatValue(0), check.compute(null));
	}

	@Test
	public void testSquareRootFloatConstant() throws Exception {
		SquareRoot check = new SquareRoot(new ConstantDescriber<>(new FloatValue(156.25f)));
		assertEquals(new FloatValue(12.5f), check.compute(null));
	}

	@Test(expected = ArithmeticException.class)
	public void testSquareRootIntLessZero() throws Exception {
		SquareRoot check = new SquareRoot(new ConstantDescriber<>(new IntValue(-1)));
		check.compute(null);
	}

	@Test(expected = ArithmeticException.class)
	public void testSquareRootFloatLessZero() throws Exception {
		SquareRoot check = new SquareRoot(new ConstantDescriber<>(new FloatValue(-123f)));
		check.compute(null);
	}
}