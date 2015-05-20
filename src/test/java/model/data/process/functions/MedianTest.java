package model.data.process.functions;

import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.Median;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.junit.Test;

import exceptions.FunctionInputMismatchException;

import static org.junit.Assert.*;

/**
 * Test for Median.
 * 
 * @author Louis Gosschalk 16-05-2015
 */
public class MedianTest extends FunctionTest {

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringMedian() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatMedian() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.35f), med);
	}

	@Test
	public void testIntMedian() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(7.0f), med);
	}

	@Test
	public void testFloatMedian2() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(7.8500004f);
		assertEquals(f, med);
	}

	@Test
	public void testIntMedian2() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(11.0f);
		assertEquals(f, med);
	}

	@Test
	public void testMedianTriplet() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, med);
	}

	@Test
	public void testMedianOdd() throws Exception {
		extendTable();

		DataValue med = new Median(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.5f), med);

		med = new Median(table, new RowValueDescriber<>(floatsColumn)).calculate();
		assertEquals(new FloatValue(6.9f), med);

		med = new Median(table, new RowValueDescriber<>(floatersColumn)).calculate();
		assertEquals(new FloatValue(6.6f), med);

		med = new Median(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(9.0f), med);

		med = new Median(table, new RowValueDescriber<>(intsColumn)).calculate();
		assertEquals(new FloatValue(12.0f), med);
	}

}
