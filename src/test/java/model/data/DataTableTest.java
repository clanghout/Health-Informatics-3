package model.data;


import model.data.value.DataValue;
import model.data.value.FloatValue;
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
		assertEquals(rows.get(0), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(rows.get(1), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(rows.get(2), iterator.next());
		assertFalse(iterator.hasNext());
	}


	@Test
	public void testFlaggedDelete() throws Exception {
		dataTable.flagNotDelete(rows.get(1));
		assertEquals(rows.get(1), dataTable.getRow(1));
		assertEquals( rows.get(2), dataTable.getRow(2));
	}

	@Test
	public void testDelete() throws Exception {
		dataTable.flagNotDelete(rows.get(1));
		dataTable.deleteNotFlagged();
		assertEquals(rows.get(1), dataTable.getRow(0));
		assertEquals(1, dataTable.getRowCount(), 0.1);
	}

	@Test
	public void testDelete2() throws Exception {
		dataTable.flagNotDelete(rows.get(0));
		dataTable.flagNotDelete(rows.get(2));
		dataTable.deleteNotFlagged();
		assertEquals(2, dataTable.getRowCount(), 0.1);
		assertTrue(dataTable.getRows().contains(rows.get(0)));
		assertTrue(dataTable.getRows().contains(rows.get(2)));
	}

	@Test
	public void testGetName() throws Exception {
		assertEquals("test", dataTable.getName());
	}

	@Test
	public void testGetColumnName() throws Exception {
		assertEquals(columns[0], dataTable.getColumn("column1"));
	}

	@Test
	public void testCopy() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		DataTable copy = table.copy();

		assertFalse(copy == table);
		assertTrue(table.equalsSoft(copy));
	}

	@Test
	public void testCopyEmptyTable() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		DataTable table = builder.build();

		DataTable copy = table.copy();

		assertFalse(copy == table);
		assertEquals(table, copy);
	}

	@Test
	public void testEquals() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		DataTableBuilder builder2 = new DataTableBuilder();
		builder.setName("t");
		builder2.setName("t");
		builder2.addColumn(builder.createColumn("column1", StringValue.class));
		builder.createRow(new StringValue("te"));
		builder2.createRow(new StringValue("te"));
		DataTable table = builder.build();

		DataTable copy =  builder2.build();

		assertTrue(table.equals(copy));
	}

	@Test
	public void testEqualsSoft() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));

		DataTable copy = builder.build();

		assertFalse(table.equals(copy));
		assertTrue(table.equalsSoft(copy));
	}

	@Test
	public void testEqualsSoftFalse() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column2", StringValue.class);
		builder.createRow(new StringValue("te"));

		DataTable copy = builder.build();

		assertFalse(table.equals(copy));
		assertFalse(table.equalsSoft(copy));
	}

	@Test
	public void testEqualsStructure() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("tawfe"));

		DataTable copy = builder.build();

		assertFalse(table.equals(copy));
		assertFalse(table.equalsSoft(copy));
		assertTrue(table.equalStructure(copy));
	}

	@Test
	public void testEqualsStructureFalse() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column2", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("tawfe"));

		DataTable copy = builder.build();

		assertFalse(table.equals(copy));
		assertFalse(table.equalsSoft(copy));
		assertFalse(table.equalStructure(copy));
	}

	@Test
	public void testEqualsStructureFalse2() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", FloatValue.class);
		builder.createRow(new FloatValue(1f));
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("tawfe"));

		DataTable copy = builder.build();

		assertFalse(table.equals(copy));
		assertFalse(table.equalsSoft(copy));
		assertFalse(table.equalStructure(copy));
	}

	@Test
	public void testEqualsStructureFalse3() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createColumn("column2", StringValue.class);
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("tawfe"));

		DataTable copy = builder.build();

		assertFalse(table.equals(copy));
		assertFalse(table.equalsSoft(copy));
		assertFalse(table.equalStructure(copy));
	}

	@Test
	public void testHasCode() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("t2");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("tawfe"));

		DataTable copy = builder.build();

		assertEquals(table.hashCode(), copy.hashCode());
	}

	@Test
	public void testExport() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		DataTable copy = table.export("t2");

		assertFalse(copy == table);
		assertEquals("t2", copy.getName());
		assertFalse(table.equals(copy));
		assertTrue(table.equalsSoft(copy));
	}

	@Test
	public void testExportCodes() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		DataRow row = builder.createRow(new StringValue("te"));
		row.addCode("test");
		DataTable table = builder.build();

		DataTable copy = table.export("t2");

		assertTrue(copy.getRow(0).containsCode("test"));
	}

	@Test
	public void testGetTable() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		assertEquals(table, table.getTable("t"));
	}


	@Test(expected = NoSuchElementException.class)
	public void testGetTableException() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("t");
		builder.createColumn("column1", StringValue.class);
		builder.createRow(new StringValue("te"));
		DataTable table = builder.build();

		table.getTable("no");
	}

}