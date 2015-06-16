package model.process.functions;

import model.exceptions.FunctionInputMismatchException;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for maximum.
 * 
 * @author Louis Gosschalk 12-05-2015
 */
public class MaximumTest extends FunctionTest {

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringMaximum() throws Exception {
		DataValue maximum = new Maximum(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.9f), max);
	}

	@Test
	public void testIntMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new IntValue(10), max);
	}

	@Test
	public void testFloatMultipleMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(8.8f);
		assertEquals(f, max);
	}

	@Test
	public void testFloatTripleMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, max);
	}

	@Test
	public void testIntMultipleMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(intsColumn)).calculate();
		IntValue f = new IntValue(12);
		assertEquals(f, max);
	}
}
