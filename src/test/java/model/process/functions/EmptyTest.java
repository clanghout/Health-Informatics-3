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
	private FloatValue f;

	@Before
	public void setUp() {
		table = new DataTable();
		f = new FloatValue(0f);
	}

	@Test
	public void testEmptyTableAverage() throws Exception {
		assertEquals(f,
				new Average(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableMaximum() throws Exception {
		assertEquals(f,
				new Maximum(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableMinimum() throws Exception {
		assertEquals(f,
				new Minimum(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableMedian() throws Exception {
		assertEquals(f,
				new Median(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableSum() throws Exception {
		assertEquals(f, new Sum(table, new RowValueDescriber<>(column)).calculate());
	}

	@Test
	public void testEmptyTableDeviation() throws Exception {
		assertEquals(f,
				new StandardDeviation(table, new RowValueDescriber<>(column)).calculate());
	}
}
