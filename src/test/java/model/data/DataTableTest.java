package model.data;


import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by jens on 4/30/15.
 */
public class DataTableTest {
	private List<DataRow> rows;
	private DataColumn[] columns;
	private DataTable dataTable;


	@Before
	public void setUp() throws Exception {
		rows = new ArrayList<DataRow>();
		columns = new DataColumn[] {
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
		rows.add(new DataRow(columns, valuesRow1));
		rows.add(new DataRow(columns, valuesRow2));
		rows.add(new DataRow(columns, valuesRow3));

		dataTable = new DataTable("test", rows, Arrays.asList(columns));
	}

	@Test
	public void testGetRow() throws Exception {
		assertEquals(dataTable.getRow(1), rows.get(1));
	}
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetNotExistingRow() throws Exception {
		dataTable.getRow(45);
	}

	@Test
	public void testGetRows() throws Exception {
		List<DataRow> rowsTable = dataTable.getRows();
		assertEquals(rowsTable.size(),rows.size());
		for (int i = 0; i < rowsTable.size(); i++) {
			DataRow rowModel = rowsTable.get(i);
			DataRow row = rows.get(i);
			assertEquals(rowModel,row);
		}
	}

	@Test
	public void testGetColumns() throws Exception {
		List<DataColumn> actual = dataTable.getColumns();
		for (DataColumn c : columns) {
			assertTrue(actual.contains(c));
		}
	}

	@Test
	public void testIterator() throws Exception {
		Iterator<DataRow> iterator = dataTable.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(iterator.next(), rows.get(0));
		assertTrue(iterator.hasNext());
		assertEquals(iterator.next(), rows.get(1));
		assertTrue(iterator.hasNext());
		assertEquals(iterator.next(), rows.get(2));
		assertFalse(iterator.hasNext());
	}


	@Test
	public void testFlaggedDelete() throws Exception {
		dataTable.flagNotDelete(rows.get(1));
		assertEquals(dataTable.getRow(1), rows.get(1));
		assertEquals(dataTable.getRow(2), rows.get(2));
	}

	@Test
	public void testDelete() throws Exception {
		dataTable.flagNotDelete(rows.get(1));
		dataTable.deleteNotFlagged();
		assertEquals(dataTable.getRow(0), rows.get(1));
		assertEquals(dataTable.getRowCount(), 1, 0.1);
	}

	@Test
	public void testDelete2() throws Exception {
		dataTable.flagNotDelete(rows.get(0));
		dataTable.flagNotDelete(rows.get(2));
		dataTable.deleteNotFlagged();
		assertEquals(dataTable.getRowCount(), 2, 0.1);
		assertTrue(dataTable.getRows().contains(rows.get(0)));
		assertTrue(dataTable.getRows().contains(rows.get(2)));
	}

	@Test
	public void testGetName() throws Exception {
		assertEquals(dataTable.getName(), "test");
	}

	@Test
	public void testGetColumnName() throws Exception {
		assertEquals(dataTable.getColumn("column1"), columns[0]);
	}
}