package model.data;

import exceptions.ColumnValueMismatchException;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 4/30/15.
 */
public class DataModelBuilderTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testCreateColumn() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		DataColumn column = builder.createColumn("test", StringValue.class);
		assertEquals(column.getName(), "test");
		assertEquals(column.getType(), StringValue.class);
	}

	@Test
	public void testCreateRow() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		DataColumn column = builder.createColumn("test", StringValue.class);
		builder.addColumn(column);
		DataRow row = builder.createRow(new StringValue("v1"));
		assertEquals(row.getValue(column).getValue(), "v1");
	}

	@Test
	public void testCreateEmptyRow() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		builder.createRow();
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testCreateRowToFewValues() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		builder.addColumn(builder.createColumn("test", StringValue.class));
		builder.addColumn(builder.createColumn("test2", StringValue.class));
		builder.createRow(new StringValue("v1"));
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testCreateRowToFewColumns() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		builder.createRow(new StringValue("v1"));
	}

	@Test
	public void testBuild() throws Exception {

	}

	@Test
	public void testAddColumn() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		assertEquals(builder.build().getColumns().size(), 0, 0.1);
		DataColumn column = builder.createColumn("test", StringValue.class);
		builder.addColumn(column);
		assertEquals(builder.build().getColumns().get("test"), column);
	}

	@Test
	public void testAddRow() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();
		builder.addColumn(builder.createColumn("test", StringValue.class));
		assertTrue(builder.build().getRows().isEmpty());

		DataRow row = builder.createRow(new StringValue("v1"));
		builder.addRow(row);
		assertEquals(builder.build().getRows().get(0),row);
	}

}