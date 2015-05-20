package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;

import static org.junit.Assert.*;

/**
 * Test for average.
 * 
 * @author Louis Gosschalk 12-05-2015
 */
public class AverageTest extends FunctionTest {

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringAverage() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatAverage() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.375f), av);
	}

	@Test
	public void testIntAverage() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(6.75f), av);
	}

	@Test
	public void testFloatAverage2() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(floatsColumn)).calculate();
		assertEquals(new FloatValue(7.45f), av);
	}

	@Test
	public void testIntAverage2() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(intsColumn)).calculate();
		assertEquals(new FloatValue(9.25f), av);
	}
}
