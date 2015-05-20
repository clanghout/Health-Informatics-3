package model.data.process.functions;

import model.data.describer.RowValueDescriber;
import model.data.process.functions.Maximum;
import model.data.value.DataValue;
import model.data.value.FloatValue;

import org.junit.Test;

import exceptions.FunctionInputMismatchException;
import static org.junit.Assert.*;

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
		assertEquals(new FloatValue(10.0f), max);
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
		FloatValue f = new FloatValue(12.0f);
		assertEquals(f, max);
	}
}
