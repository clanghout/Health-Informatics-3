package model.data;

import model.process.describer.OperationDescriber;
import model.process.describer.RowValueDescriber;
import model.process.analysis.ConstraintAnalysis;
import model.process.analysis.operations.constraints.Constraint;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.data.value.DataValue;
import model.data.value.IntValue;
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
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		rows = new ArrayList<>();
		dataTables = new ArrayList<>();
		columns[0] = new DataColumn[]{
				builder.createColumn("column1", StringValue.class),
				builder.createColumn("column2", StringValue.class),
				builder.createColumn("column3", StringValue.class)
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
		rows.add(builder.createRow(valuesRow1));
		rows.add(builder.createRow(valuesRow2));
		rows.add(builder.createRow(valuesRow3));

		rows.get(1).addCode("test1");
		rows.get(1).addCode("test2");
		rows.get(2).addCode("test2");

		dataTables.add(builder.build());

		builder = new DataTableBuilder();
		builder.setName("test2");
		rows = new ArrayList<>();
		columns[1] = new DataColumn[]{
				builder.createColumn("column1", StringValue.class),
				builder.createColumn("column2", StringValue.class)
		};

		DataValue[] valuesRow4 = {
				new StringValue("awfg"),
				new StringValue("fsa")
		};

		DataValue[] valuesRow5 = {
				new StringValue("sfa"),
				new StringValue("asf")
		};

		rows.add(builder.createRow(valuesRow4));
		rows.add(builder.createRow(valuesRow5));

		rows.get(1).addCode("test3");


		dataTables.add(builder.build());
		builder = new DataTableBuilder();
		builder.setName("test3");

		rows = new ArrayList<>();
		columns[2] = new DataColumn[]{
				builder.createColumn("column1", StringValue.class)
		};

		DataValue[] valuesRow6 = {
				new StringValue("ewa")
		};

		rows.add(builder.createRow(valuesRow6));


		dataTables.add(builder.build());

	}

	@Test
	public void testIterator() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1));
		Iterator<? extends Row> it = comb.iterator();
		assertTrue(it.hasNext());
		assertEquals("awfg", it.next().getValue(columns[1][0]).toString());
		assertTrue(it.hasNext());
		assertEquals("sfa", it.next().getValue(columns[1][0]).toString());
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
		assertEquals("value2", row.getValue(columns[0][1]).toString());
		assertEquals("fsa", row.getValue(columns[1][1]).toString());
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals("value2b", row.getValue(columns[0][1]).toString());
		assertEquals("fsa", row.getValue(columns[1][1]).toString());
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals("value2c", row.getValue(columns[0][1]).toString());
		assertEquals("fsa", row.getValue(columns[1][1]).toString());
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals("value2", row.getValue(columns[0][1]).toString());
		assertEquals("asf", row.getValue(columns[1][1]).toString());
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals("value2b", row.getValue(columns[0][1]).toString());
		assertEquals("asf", row.getValue(columns[1][1]).toString());
		assertTrue(it.hasNext());
		row = it.next();
		assertEquals("value2c", row.getValue(columns[0][1]).toString());
		assertEquals("asf", row.getValue(columns[1][1]).toString());
		assertFalse(it.hasNext());
	}


	@Test
	public void testDelete() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		Iterator<? extends Row> it = comb.iterator();
		comb.flagNotDelete(it.next());
		it.next();
		it.next();
		comb.flagNotDelete(it.next());
		it.next();
		comb.flagNotDelete(it.next());

		comb.deleteNotFlagged();

		assertEquals(2, dataTables.get(0).getRowCount());
		assertEquals(2, dataTables.get(1).getRowCount());
		assertEquals(1, dataTables.get(2).getRowCount());
	}

	@Test
	public void testDelete2() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		Iterator<? extends Row> it = comb.iterator();
		comb.flagNotDelete(it.next());
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();

		comb.deleteNotFlagged();

		assertEquals(1, dataTables.get(0).getRowCount());
		assertEquals(1, dataTables.get(1).getRowCount());
		assertEquals(1, dataTables.get(2).getRowCount());
	}


	@Test
	public void testConstrantOverMultipleRows() throws Exception {
		DataTableBuilder builder1 = new DataTableBuilder();
		builder1.setName("table1");
		DataColumn column1 = builder1.createColumn("column1", IntValue.class);
		DataRow[] rows1 = new DataRow[5];
		for (int i = 0; i < 5; i++) {
			rows1[i] = builder1.createRow(new IntValue(i));
		}

		DataTable table1 = builder1.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table1");
		DataColumn column2 = builder2.createColumn("column2", IntValue.class);
		DataRow[] rows2 = new DataRow[5];
		for (int i = 0; i < 10; i = i + 2) {
			rows2[i/2] = builder2.createRow(new IntValue(i));
		}

		DataTable table2 = builder2.build();

		CombinedDataTable comb = new CombinedDataTable(table1, table2);

		Constraint columnCheck = new EqualityCheck<>(
				new RowValueDescriber<>(column1),
				new RowValueDescriber<>(column2)
		);

		ConstraintAnalysis analysis = new ConstraintAnalysis(new OperationDescriber<>(columnCheck));

		assertEquals(5, table1.getRowCount());
		assertEquals(5, table2.getRowCount());
		analysis.analyse(comb);

		assertEquals(3, table1.getRowCount());
		assertEquals(3, table2.getRowCount());

		table1.getRows();

		assertTrue(table1.getRows().contains(rows1[0]));
		assertTrue(table1.getRows().contains(rows1[2]));
		assertTrue(table1.getRows().contains(rows1[4]));

		assertTrue(table2.getRows().contains(rows2[0]));
		assertTrue(table2.getRows().contains(rows2[1]));
		assertTrue(table2.getRows().contains(rows2[2]));
	}

	@Test
	public void testCopy() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		CombinedDataTable copy = comb.copy();

		assertTrue(comb.equalsSoft(copy));
	}

	@Test
	public void testEquals() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		CombinedDataTable copy = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));

		assertTrue(comb.equals(copy));
	}


	@Test
	public void testEqualsSoft() throws Exception {

		DataTableBuilder builder = new DataTableBuilder();
		DataTableBuilder builder2 = new DataTableBuilder();
		builder.setName("t1");
		builder2.setName("t2");

		builder.createColumn("t1", StringValue.class);
		builder2.createColumn("t1", StringValue.class);

		DataRow row1 = builder.createRow(new StringValue("test"));
		DataRow row1C = builder2.createRow(new StringValue("test"));

		DataRow row2 = builder.createRow(new StringValue("test2"));
		DataRow row2C = builder2.createRow(new StringValue("test2"));

		CombinedDataTable comb = new CombinedDataTable(builder.build());
		CombinedDataTable copy = new CombinedDataTable(builder2.build());

		assertFalse(comb.equals(copy));
		assertTrue(comb.equalsSoft(copy));
	}

	@Test
	public void testHashCode() throws Exception {

		DataTableBuilder builder = new DataTableBuilder();
		DataTableBuilder builder2 = new DataTableBuilder();
		builder.setName("t1");
		builder2.setName("t2");

		builder.createColumn("t1", StringValue.class);
		builder2.createColumn("t1", StringValue.class);

		DataRow row1 = builder.createRow(new StringValue("test"));
		DataRow row1C = builder2.createRow(new StringValue("test"));

		DataRow row2 = builder.createRow(new StringValue("test2"));
		DataRow row2C = builder2.createRow(new StringValue("test2"));

		CombinedDataTable comb = new CombinedDataTable(builder.build());
		CombinedDataTable copy = new CombinedDataTable(builder2.build());

		assertEquals(comb.hashCode(), copy.hashCode());
	}

	@Test
	public void testgetColumns() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		List<DataColumn> columnsList = comb.getColumns();
		assertEquals(columnsList.size(), columns[0].length + columns[1].length + columns[2].length);
		assertTrue(columnsList.contains(columns[1][1]));
		assertTrue(columnsList.contains(columns[0][0]));
		assertTrue(columnsList.contains(columns[2][0]));
	}

	@Test
	public void testExport() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		DataTable copy = comb.export("test");

		List<DataRow> rowsCopy = copy.getRows();
		assertEquals("value1", rowsCopy.get(0).getValue(copy.getColumn("test1_column1")).toString());
		assertEquals("fsa", rowsCopy.get(0).getValue(copy.getColumn("test2_column2")).toString());
		assertEquals("ewa", rowsCopy.get(0).getValue(copy.getColumn("test3_column1")).toString());

		assertEquals("value1b", rowsCopy.get(1).getValue(copy.getColumn("test1_column1")).toString());
		assertEquals("fsa", rowsCopy.get(1).getValue(copy.getColumn("test2_column2")).toString());
		assertEquals("ewa", rowsCopy.get(1).getValue(copy.getColumn("test3_column1")).toString());

		assertEquals("value3", rowsCopy.get(3).getValue(copy.getColumn("column3")).toString());
		assertEquals("asf", rowsCopy.get(3).getValue(copy.getColumn("test2_column2")).toString());
		assertEquals("ewa", rowsCopy.get(3).getValue(copy.getColumn("test3_column1")).toString());

		assertEquals("value2c", rowsCopy.get(5).getValue(copy.getColumn("test1_column2")).toString());
		assertEquals("sfa", rowsCopy.get(5).getValue(copy.getColumn("test2_column1")).toString());
		assertEquals("ewa", rowsCopy.get(5).getValue(copy.getColumn("test3_column1")).toString());

		assertEquals("test", copy.getName());
	}

	@Test
	public void testExportCodes() throws Exception {

		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		DataTable copy = comb.export("test");

		List<DataRow> rowsCopy = copy.getRows();
		assertFalse(rowsCopy.get(0).containsCode("test1"));
		assertFalse(rowsCopy.get(0).containsCode("test2"));
		assertFalse(rowsCopy.get(0).containsCode("test3"));

		assertTrue(rowsCopy.get(1).containsCode("test1"));
		assertTrue(rowsCopy.get(1).containsCode("test2"));
		assertFalse(rowsCopy.get(1).containsCode("test3"));

		assertFalse(rowsCopy.get(2).containsCode("test1"));
		assertTrue(rowsCopy.get(2).containsCode("test2"));
		assertFalse(rowsCopy.get(2).containsCode("test3"));

		assertTrue(rowsCopy.get(4).containsCode("test1"));
		assertTrue(rowsCopy.get(4).containsCode("test2"));
		assertTrue(rowsCopy.get(4).containsCode("test3"));


	}

	@Test
	public void testGetTables() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(
				dataTables.get(1),
				dataTables.get(0),
				dataTables.get(2));
		List<DataTable> list = comb.getTables();

		assertEquals(dataTables.get(1), list.get(0));
		assertEquals(dataTables.get(0), list.get(1));
		assertEquals(dataTables.get(2), list.get(2));
	}

	@Test
	public void testGetTable() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		assertEquals(dataTables.get(0), comb.getTable("test1"));
		assertEquals(dataTables.get(1), comb.getTable("test2"));
		assertEquals(dataTables.get(2), comb.getTable("test3"));
	}


	@Test(expected = NoSuchElementException.class)
	public void testGetTableException() throws Exception {
		CombinedDataTable comb = new CombinedDataTable(dataTables.get(1), dataTables.get(0), dataTables.get(2));
		comb.getTable("no");
	}

}