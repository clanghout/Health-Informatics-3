package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 4/29/15.
 */
public class DataRowTest {

	@Test
	public void testConstructor() throws Exception {
		DataColumn[] columns = new DataColumn[3];
		DataValue[] values = new DataValue[3];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);
		columns[2] = new DataColumn("c", null, DataValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		values[2] = new StringValue("test3");
		DataRow row = new DataRow(columns, values);

		assertEquals(row.getValue(columns[0]).getValue(), "test1");
		assertEquals(row.getValue(columns[1]).getValue(), "test2");
		assertEquals(row.getValue(columns[2]).getValue(), "test3");
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testConstructorMismatchNotEnoughValues() throws Exception {
		DataColumn[] columns = new DataColumn[3];
		DataValue[] values = new DataValue[2];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);
		columns[2] = new DataColumn("c", null, StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		DataRow row = new DataRow(columns, values);
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testConstructorMismatchNotEnoughColumns() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[3];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		values[2] = new StringValue("test3");
		DataRow row = new DataRow(columns, values);
	}

	//TODO when we have more types, give value[1] a type
	@Test(expected = ColumnValueTypeMismatchException.class)
	public void testConstructorTypeMismatch() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);

		values[0] = new StringValue("test1");
		DataRow row = new DataRow(columns, values);
	}

	@Test
	public void testSetValue() throws Exception {
		DataRow row = new DataRow();
		DataColumn[] columns = new DataColumn[2];
		columns[0] = new DataColumn("a", null, StringValue.class);

		row.setValue(columns[0], new StringValue("test1"));
		assertEquals(row.getValue(columns[0]).getValue(), "test1");
	}

	@Test
	public void testSetExistingValue() throws Exception {
		DataRow row = new DataRow();
		DataColumn[] columns = new DataColumn[2];
		columns[0] = new DataColumn("a", null, StringValue.class);

		row.setValue(columns[0], new StringValue("test1"));
		assertEquals(row.getValue(columns[0]).getValue(), "test1");

		row.setValue(columns[0], new StringValue("test2"));
		assertEquals(row.getValue(columns[0]).getValue(), "test2");
	}

	@Test
	public void testGetNotExistingValue() throws Exception {
		DataRow row = new DataRow();
		DataColumn column = new DataColumn("a", null, StringValue.class);

		assertEquals(row.getValue(column), null);

	}

	@Test
	public void testHasColumnTrue() throws Exception {
		DataColumn[] columns = new DataColumn[3];
		DataValue[] values = new DataValue[3];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);
		columns[2] = new DataColumn("c", null, DataValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		values[2] = new StringValue("test3");
		DataRow row = new DataRow(columns, values);

		assertTrue(row.hasColumn(columns[2]));
		assertTrue(row.hasColumn(columns[1]));
		assertTrue(row.hasColumn(columns[0]));
	}

	@Test
	public void testHasColumnFalse() throws Exception {
		DataColumn column = new DataColumn("c", null, DataValue.class);

		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");

		DataRow row = new DataRow(columns, values);

		assertFalse(row.hasColumn(column));
	}

	@Test
	public void testCopy() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");

		DataRow row = new DataRow(columns, values);
		DataRow copy = row.copy();
		assertEquals(copy.getValue(columns[0]), row.getValue(columns[0]));
	}

	@Test
	public void testCopyTable() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		DataTableBuilder builderCopy = new DataTableBuilder();
		builder.setName("t1");
		builderCopy.setName("t1C");
		DataColumn[] columns = new DataColumn[2];
		DataColumn[] columnsCopy  = new DataColumn[2];

		columns[0] = builder.createColumn("a", StringValue.class);
		columns[1] = builder.createColumn("b", StringValue.class);

		columnsCopy[0] = builderCopy.createColumn("a", StringValue.class);
		columnsCopy[1] = builderCopy.createColumn("b", StringValue.class);

		DataValue[] values = new DataValue[2];


		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");

		DataRow row = builder.createRow(values);
		builder.build();
		DataTable copyTable = builderCopy.build();
		Row copy = row.copy(copyTable);
		assertEquals(copy.getValue(columns[0]), row.getValue(columnsCopy[0]));
	}

	@Test
	public void testEqual() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		columns[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns[1] = new DataColumn("b", new DataTable(), StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");

		DataRow row = new DataRow(columns, values);
		DataRow copy = row.copy();
		assertTrue(row.equals(copy));
	}

	@Test
	public void testEqualsFalse() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		DataColumn[] columns2 = new DataColumn[2];
		DataValue[] values2 = new DataValue[2];
		columns[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns[1] = new DataColumn("b", new DataTable(), StringValue.class);
		columns2[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns2[1] = new DataColumn("b", new DataTable(), StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		values2[0] = new StringValue("test1");
		values2[1] = new StringValue("test2");

		DataRow row = new DataRow(columns, values);
		DataRow copy = new DataRow(columns2, values2);
		assertFalse(row.equals(copy));
	}

	@Test
	public void testSoftEqual() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		DataColumn[] columns2 = new DataColumn[2];
		DataValue[] values2 = new DataValue[2];
		columns[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns[1] = new DataColumn("b", new DataTable(), StringValue.class);
		columns2[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns2[1] = new DataColumn("b", new DataTable(), StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		values2[0] = new StringValue("test1");
		values2[1] = new StringValue("test2");

		DataRow row = new DataRow(columns, values);
		DataRow copy = new DataRow(columns2, values2);
		assertTrue(row.equalsSoft(copy));
	}

	@Test
	public void testHasCode() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[2];
		DataColumn[] columns2 = new DataColumn[2];
		DataValue[] values2 = new DataValue[2];
		columns[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns[1] = new DataColumn("b", new DataTable(), StringValue.class);
		columns2[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns2[1] = new DataColumn("b", new DataTable(), StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		values2[0] = new StringValue("test1");
		values2[1] = new StringValue("test2");

		DataRow row = new DataRow(columns, values);
		DataRow copy = new DataRow(columns2, values2);
		assertEquals(row.hashCode(), copy.hashCode());
	}



}