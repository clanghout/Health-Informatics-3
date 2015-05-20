package model.data;

import exceptions.ColumnValueMismatchException;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 4/30/15.
 */
public class DataTableBuilderTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testCreateColumn() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");

		DataColumn column = builder.createColumn("test", StringValue.class);
		assertEquals(column.getName(), "test");
		assertEquals(column.getType(), StringValue.class);
	}

	@Test
	public void testCreateRow() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");

		DataColumn column = builder.createColumn("test", StringValue.class);
		DataRow row = builder.createRow(new StringValue("v1"));
		assertEquals(row.getValue(column).getValue(), "v1");
	}

	@Test
	public void testCreateEmptyRow() throws Exception {
		DataTableBuilder builder =  new DataTableBuilder();
		builder.setName("test");

		builder.createRow();
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testCreateRowToFewValues() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");

		builder.addColumn(builder.createColumn("test", StringValue.class));
		builder.addColumn(builder.createColumn("test2", StringValue.class));
		builder.createRow(new StringValue("v1"));
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testCreateRowToFewColumns() throws Exception {
		DataTableBuilder builder =  new DataTableBuilder();
		builder.setName("test");

		builder.createRow(new StringValue("v1"));
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateNoName() throws Exception {
		DataTableBuilder builder =  new DataTableBuilder();
		builder.build();
	}

	@Test
	public void testBuild() throws Exception {

	}

	@Test
	public void testAddColumn() throws Exception {
		DataTableBuilder builder =  new DataTableBuilder();
		builder.setName("test");

		assertEquals(builder.build().getColumns().size(), 0, 0.1);
		DataColumn column = builder.createColumn("test", StringValue.class);
		builder.addColumn(column);
		assertEquals(builder.build().getColumns().get("test"), column);
	}

	@Test
	public void testAddRow() throws Exception {
		DataTableBuilder builder =  new DataTableBuilder();
		builder.setName("test");
		DataColumn[] column = new DataColumn[1];
		DataValue[] values = new DataValue[1];
		column[0] = builder.createColumn("test", StringValue.class);
		assertTrue(builder.build().getRows().isEmpty());
		values[0] = new StringValue("v1");

		DataRow row = new DataRow(column, values);
		builder.addRow(row);
		assertEquals(builder.build().getRows().get(0),row);
	}

}