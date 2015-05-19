package model.data;

import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/18/15.
 */
public class CombinedDataTableTest {
	private List<DataRow> rows;
	private DataColumn[] columns;
	private List<DataTable> dataTables;

	@Before
	public void setUp() throws Exception {
		rows = new ArrayList<DataRow>();
		dataTables = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", StringValue.class),
				new DataColumn("column2", StringValue.class),
				new DataColumn("column3", StringValue.class)
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
		rows.add(new DataRow(columns, valuesRow1));
		rows.add(new DataRow(columns, valuesRow2));
		rows.add(new DataRow(columns, valuesRow3));

		dataTables.add("test1", new DataTable(rows, Arrays.asList(columns)));


		rows = new ArrayList<DataRow>();
		columns = new DataColumn[]{
				new DataColumn("column1", StringValue.class),
				new DataColumn("column2", StringValue.class)
		};

		DataValue[] valuesRow4 = {
				new StringValue("awfg"),
				new StringValue("fsa")
		};

		DataValue[] valuesRow5 = {
				new StringValue("sfa"),
				new StringValue("asf")
		};

		rows.add(new DataRow(columns, valuesRow4));
		rows.add(new DataRow(columns, valuesRow5));


		dataTables.add(new DataTable("test2", rows, Arrays.asList(columns)));

		rows = new ArrayList<DataRow>();
		columns = new DataColumn[]{
				new DataColumn("column1", StringValue.class)
		};

		DataValue[] valuesRow6 = {
				new StringValue("ewa")
		};

		rows.add(new DataRow(columns, valuesRow6));


		dataTables.add(new DataTable("test3", rows, Arrays.asList(columns)));

	}

	@Test
	public void testIterator() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1));
		Iterator<CombinedDataRow> it = comb.iterator();
		assertTrue(it.hasNext());
		assertEquals(it.next().getRow("test2").getValue("column1").toString(), "awfg");
		assertTrue(it.hasNext());
		assertEquals(it.next().getRow("test2").getValue("column1").toString(), "sfa");
		assertFalse(it.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorException() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1));
		Iterator<CombinedDataRow> it = comb.iterator();
		it.next();
		it.next();
		it.next();
	}

	@Test
	public void testMultipleIterator() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0));
		Iterator<CombinedDataRow> it = comb.iterator();
		assertTrue(it.hasNext());
		CombinedDataRow row;
		row = it.next();
		assertEquals(row.getRow("test2").getValue("column1").toString(), "awfg");
		assertTrue(it.hasNext());
		assertEquals(row.getRow("test1").getValue("column1").toString(), "value1");
		row = it.next();
		assertEquals(row.getRow("test1").getValue("column1").toString(), "value1b");
		assertTrue(it.hasNext());
		assertEquals(row.getRow("test2").getValue("column1").toString(), "awfg");
		row = it.next();
		assertEquals(row.getRow("test2").getValue("column1").toString(), "awfg");
		assertTrue(it.hasNext());
		assertEquals(row.getRow("test1").getValue("column1").toString(), "value1c");
		row = it.next();
		assertEquals(row.getRow("test1").getValue("column1").toString(), "value1");
		assertEquals(row.getRow("test2").getValue("column1").toString(), "sfa");
		row = it.next();
		assertEquals(row.getRow("test2").getValue("column1").toString(), "sfa");
		assertEquals(row.getRow("test1").getValue("column1").toString(), "value1b");
		row = it.next();
		assertEquals(row.getRow("test1").getValue("column1").toString(), "value1c");
		assertEquals(row.getRow("test2").getValue("column1").toString(), "sfa");
		assertFalse(it.hasNext());
	}
}