package model.process.functions;

import model.data.value.DateTimeValue;
import model.process.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for Minimum.
 * 
 * @author Louis Gosschalk 12-05-2015
 */
public class MinimumTest extends FunctionTest {

	@Test
	public void testDateMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(dateColumn)).calculate();
		assertEquals(new DateTimeValue(1995, 1, 17, 0, 0, 0), min);
	}

	@Test
	public void testFloatMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(5.9f), min);
	}

	@Test
	public void testIntMinimum() throws Exception {
		DataValue min = new Minimum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new IntValue(3), min);
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
		IntValue f = new IntValue(3);
		assertEquals(f, min);
	}
}
