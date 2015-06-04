package model.process.functions;

import model.exceptions.InputMismatchException;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for Minimum.
 * 
 * @author Louis Gosschalk 12-05-2015
 */
public class MinimumTest extends FunctionTest {

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = InputMismatchException.class)
	public void TestStringMinimum() throws Exception {
		DataValue minimum = new Minimum(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(5.9f), min);
	}

	@Test
	public void testIntMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(3.0f), min);
	}

	@Test
	public void testFloatMultipleMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(5.3f);
		assertEquals(f, min);
	}

	@Test
	public void testFloatTripleMinimum() throws Exception {
		DataValue max = new Minimum(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(6.4f);
		assertEquals(f, max);
	}

	@Test
	public void testIntMultipleMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(3.0f);
		assertEquals(f, min);
	}
}
