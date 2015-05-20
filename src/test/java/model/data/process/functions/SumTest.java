package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.Sum;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;

import static org.junit.Assert.*;

/**
 * Test for sum.
 * 
 * @author Louis Gosschalk 16-05-2015
 */
public class SumTest extends FunctionTest {

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringsum() throws Exception {
		DataValue sum = new Sum(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatsum() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(25.5f), sm);
	}

	@Test
	public void testIntsum() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(27), sm);
	}

	@Test
	public void testFloatSum2() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(29.8f);
		assertEquals(f, sm);
	}

	@Test
	public void testFloatSum3() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(26.2f);
		assertEquals(f, sm);
	}

	@Test
	public void testIntSum2() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(37.0f);
		assertEquals(f, sm);
	}
}
