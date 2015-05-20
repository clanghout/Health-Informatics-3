package model.data;

import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/19/15.
 */
public class CombinedDataTableTest {
	private List<DataRow> rows;
	private DataColumn[][] columns = new DataColumn[3][];
	private List<DataTable> dataTables;

	@Before
	public void setUp() throws Exception {
		rows = new ArrayList<DataRow>();
		dataTables = new ArrayList<>();
		columns[0] = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class),
				new DataColumn("column2", null, StringValue.class),
				new DataColumn("column3", null, StringValue.class)
		};

		DataValue[] valuesRow1 = {
				new StringValue("value1"),
				new StringValue("value2"),
				new StringValue("value3")
		};

		DataValue[] valuesRow2 = {
				new StringValue("value1b"),
				new StringValue("value2b"),
				new StringValue("value3b")
		};

		DataValue[] valuesRow3 = {
				new StringValue("value1c"),
				new StringValue("value2c"),
				new StringValue("value3c")
		};
		rows.add(new DataRow(columns[0], valuesRow1));
		rows.add(new DataRow(columns[0], valuesRow2));
		rows.add(new DataRow(columns[0], valuesRow3));

		dataTables.add(new DataTable("test1", rows, Arrays.asList(columns[0])));


		rows = new ArrayList<DataRow>();
		columns[1] = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class),
				new DataColumn("column2", null, StringValue.class)
		};

		DataValue[] valuesRow4 = {
				new StringValue("awfg"),
				new StringValue("fsa")
		};

		DataValue[] valuesRow5 = {
				new StringValue("sfa"),
				new StringValue("asf")
		};

		rows.add(new DataRow(columns[1], valuesRow4));
		rows.add(new DataRow(columns[1], valuesRow5));


		dataTables.add(new DataTable("test2", rows, Arrays.asList(columns[1])));

		rows = new ArrayList<DataRow>();
		columns[2] = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class)
		};

		DataValue[] valuesRow6 = {
				new StringValue("ewa")
		};

		rows.add(new DataRow(columns[2], valuesRow6));


		dataTables.add(new DataTable("test3", rows, Arrays.asList(columns[2])));

	}

	@Test
	public void testIterator() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1));
		Iterator<? extends Row> it = comb.iterator();
		assertTrue(it.hasNext());
		assertEquals(it.next().getValue(columns[1][0]).toString(), "awfg");
		assertTrue(it.hasNext());
		assertEquals(it.next().getValue(columns[1][0]).toString(), "sfa");
		assertFalse(it.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorException() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1));
		Iterator<? extends Row> it = comb.iterator();
		it.next();
		it.next();
		it.next();
	}

	@Test
	public void testMultipleIterator() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0));
		Iterator<? extends Row> it = comb.iterator();
		assertTrue(it.hasNext());
		Row row;
		row = it.next();
		assertEquals(row.getValue(columns[0][1]).toString(), "value2");
		assertEquals(row.getValue(columns[1][1]).toString(), "fsa");
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals(row.getValue(columns[0][1]).toString(), "value2b");
		assertEquals(row.getValue(columns[1][1]).toString(), "fsa");
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals(row.getValue(columns[0][1]).toString(), "value2c");
		assertEquals(row.getValue(columns[1][1]).toString(), "fsa");
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals(row.getValue(columns[0][1]).toString(), "value2");
		assertEquals(row.getValue(columns[1][1]).toString(), "asf");
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals(row.getValue(columns[0][1]).toString(), "value2b");
		assertEquals(row.getValue(columns[1][1]).toString(), "asf");
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals(row.getValue(columns[0][1]).toString(), "value2c");
		assertEquals(row.getValue(columns[1][1]).toString(), "asf");
		assertFalse(it.hasNext());
	}
}