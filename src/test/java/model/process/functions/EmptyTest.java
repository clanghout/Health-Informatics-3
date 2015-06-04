package model.process.functions;

import model.data.value.FloatValue;
import model.exceptions.FunctionInputMismatchException;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.describer.RowValueDescriber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Standard tests which should pass on all types of functions.
 * 
 * @author Louis Gosschalk 19-05-2015
 */
public class EmptyTest {
	private DataTable table;
	private DataColumn column;

	@Before
	public void setUp() {
		table = new DataTable();
	}

	@Test
	public void testEmptyTableAverage() throws Exception {
		assertEquals(new FloatValue(0),
				new Average(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableMaximum() throws Exception {
		assertEquals(new FloatValue(0),
				new Maximum(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableMinimum() throws Exception {
		assertEquals(new FloatValue(0),
				new Minimum(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableMedian() throws Exception {
		assertEquals(new FloatValue(0),
				new Median(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableSum() throws Exception {
		assertEquals(new FloatValue(0), new Sum(table, new RowValueDescriber<>(column)).calculate());
	}

	public void testEmptyTableDeviation() throws Exception {
		assertEquals(new FloatValue(0),
				new StandardDeviation(table, new RowValueDescriber<>(column)).calculate());
	}
}
