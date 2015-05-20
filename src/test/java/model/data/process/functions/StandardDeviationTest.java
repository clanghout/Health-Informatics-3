package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.StandardDeviation;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;
import static org.junit.Assert.*;

/**
 * Test for Standard Deviation.
 * 
 * @author Louis Gosschalk 12-05-2015
 */
public class StandardDeviationTest extends FunctionTest {

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringStandardDeviation() throws Exception {
		DataValue StandardDeviation = new StandardDeviation(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(0.36996624f), std);
	}

	@Test
	public void testIntStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(2.8613808f), std);
	}

	@Test
	public void testFloatMultipleStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(1.4637281f);
		assertEquals(f, std);
	}

	@Test
	public void testFloatTripleStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(0.08660246f);
		assertEquals(f, std);
	}

	@Test
	public void testIntMultipleStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(3.6996622f);
		assertEquals(f, std);
	}
}
