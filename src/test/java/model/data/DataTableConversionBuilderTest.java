package model.data;

import model.data.value.StringValue;
import model.exceptions.ColumnValueTypeMismatchException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/29/15.
 */
public class DataTableConversionBuilderTest {
	private DataTable table1;
	private DataTable table2;
	private DataTable table3;

	@Before
	public void setup(){
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		DataRow row1 = builder.createRow(new StringValue("test"), new StringValue("test2"));
		row1.addCode("test");
		builder.createRow(new StringValue("test21"), new StringValue("test22"));

		builder.setName("table2");

		table1 = builder.build();

		builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		DataRow row2 = builder.createRow(new StringValue("atest"), new StringValue("atest2"));
		row2.addCode("test221");
		builder.createRow(new StringValue("atest21"), new StringValue("atest22"));

		builder.setName("table1");

		table2 = builder.build();

		builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("atest21"));

		builder.setName("table1");

		table3 = builder.build();

	}
	@Test
	public void testAddRowsFromTable() throws Exception {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table1, table1.getName());
		builder.addRowsFromTable(table1);

		DataTable res = builder.build();

		assertNotEquals(res.getRow(0), table1.getRow(0));
		assertTrue(res.getRow(0).equalsSoft(table1.getRow(0)));
		assertTrue(res.getRow(0).containsCode("test"));

		assertNotEquals(res.getRow(1), table1.getRow(1));
		assertTrue(res.getRow(1).equalsSoft(table1.getRow(1)));
		assertFalse(res.getRow(1).containsCode("test"));
	}

	@Test
	public void testGetRows() throws Exception {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table1, table1.getName());
		builder.addRowsFromTable(table1);

		DataTable res = builder.build();
		assertEquals(res.getRows(), builder.getRows());
	}

	@Test
	public void testCreateRow() throws Exception {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table1, table1.getName());
		builder.addRowsFromTable(table1);

		builder.createRow(table2.getRow(0));
		DataTable res = builder.build();

		assertNotEquals(res.getRow(0), table1.getRow(0));
		assertTrue(res.getRow(0).equalsSoft(table1.getRow(0)));
		assertTrue(res.getRow(0).containsCode("test"));

		assertNotEquals(res.getRow(1), table1.getRow(1));
		assertTrue(res.getRow(1).equalsSoft(table1.getRow(1)));
		assertFalse(res.getRow(1).containsCode("test"));

		assertNotEquals(res.getRow(2), table2.getRow(0));
		assertTrue(res.getRow(2).equalsSoft(table2.getRow(0)));
		assertTrue(res.getRow(2).containsCode("test221"));
	}

	@Test(expected = ColumnValueTypeMismatchException.class)
	public void testCreateRowException() throws Exception {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table1, table1.getName());
		builder.addRowsFromTable(table1);

		builder.createRow(table3.getRow(0));
	}

	@Test(expected = ColumnValueTypeMismatchException.class)
	public void testAddRowsFromTableException() throws Exception {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table1, table1.getName());
		builder.addRowsFromTable(table3);
	}
}