package model.process.setOperations;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.FloatValue;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/4/15.
 */
public class DifferenceTest {

	@Test(expected = Exception.class)
	public void testIllegalDifference() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("test"));
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.createColumn("c3", StringValue.class);
		builder2.createRow(new StringValue("test2"));
		builder2.setName("table2");

		DataTable table2 = builder2.build();
		Difference diff = new Difference(table1,table2);
	}

	@Test(expected = Exception.class)
	public void testIllegalDifference2() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("test"));
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.createColumn("c1", FloatValue.class);
		builder2.createRow(new FloatValue(2));
		builder2.setName("table2");

		DataTable table2 = builder2.build();
		Difference diff = new Difference(table1,table2);
	}

	@Test
	public void testDoProcess() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("test"));
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);
		builder2.createRow(new StringValue("test2"));

		DataTable table2 = builder2.build();
		Difference diff = new Difference(table1,table2);

		DataTable table3 = diff.doProcess();

		assertEquals(1, table3.getRowCount());
		assertEquals(1, table2.getRowCount());
		assertEquals(1, table1.getRowCount());
		assertEquals("test", table3.getRow(0).getValue(table3.getColumn("c1")).getValue());
	}

	@Test
	public void testDoProcessDiffSameRow() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("test"));
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);
		builder2.createRow(new StringValue("test"));

		DataTable table2 = builder2.build();
		Difference diff = new Difference(table1,table2);

		DataTable table3 = diff.doProcess();

		assertEquals(0, table3.getRowCount());
	}

	@Test
	public void testDoProcessDiffEmptyDiff() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		DataRow row1 = builder.createRow(new StringValue("test"));
		row1.addCode("test");
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);

		DataTable table2 = builder2.build();

		Difference diff = new Difference(table1,table2);

		DataTable table3 = diff.doProcess();

		assertEquals(1, table3.getRowCount());
		assertEquals("test", table3.getRow(0).getValue(table3.getColumn("c1")).getValue());
		assertTrue(table3.getRow(0).containsCode("test"));
	}

	@Test
	public void testDoProcessDiffEmpty() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		DataRow row1 = builder.createRow(new StringValue("test"));
		row1.addCode("test");
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);

		DataTable table2 = builder2.build();

		Difference diff = new Difference(table2, table1);

		DataTable table3 = diff.doProcess();

		assertEquals(0, table3.getRowCount());
	}


	@Test
	public void testDoProcessDiffMultipleRows() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		DataRow row1 = builder.createRow(new StringValue("test"));
		builder.createRow(new StringValue("test2"));
		builder.createRow(new StringValue("test3"));
		builder.createRow(new StringValue("test4"));
		builder.createRow(new StringValue("test5"));
		row1.addCode("test");
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);
		builder2.createRow(new StringValue("test4"));
		builder2.createRow(new StringValue("test7"));
		builder2.createRow(new StringValue("test2"));
		builder2.createRow(new StringValue("test9"));

		DataTable table2 = builder2.build();

		Difference diff = new Difference(table1, table2);

		DataTable table3 = diff.doProcess();

		assertEquals(3, table3.getRowCount());
		assertEquals("test", table3.getRow(0).getValue(table3.getColumn("c1")).getValue());
		assertEquals("test3", table3.getRow(1).getValue(table3.getColumn("c1")).getValue());
		assertEquals("test5", table3.getRow(2).getValue(table3.getColumn("c1")).getValue());
	}

}