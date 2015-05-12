package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jens on 4/29/15.
 */
public class DataRowTest {

	@Test
	public void testConstructor() throws Exception {
		DataColumn[] columns = new DataColumn[3];
		DataValue[] values = new DataValue[3];
		columns[0] = new DataColumn("a", StringValue.class);
		columns[1] = new DataColumn("b", StringValue.class);
		columns[2] = new DataColumn("c", DataValue.class);

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
		columns[0] = new DataColumn("a", StringValue.class);
		columns[1] = new DataColumn("b", StringValue.class);
		columns[2] = new DataColumn("c", StringValue.class);

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		DataRow row = new DataRow(columns, values);
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testConstructorMismatchNotEnoughColumns() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		DataValue[] values = new DataValue[3];
		columns[0] = new DataColumn("a", StringValue.class);
		columns[1] = new DataColumn("b", StringValue.class);

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
		columns[0] = new DataColumn("a", StringValue.class);
		columns[1] = new DataColumn("b", StringValue.class);

		values[0] = new StringValue("test1");
		DataRow row = new DataRow(columns, values);
	}

	@Test
	public void testSetValue() throws Exception {
		DataRow row = new DataRow();
		DataColumn[] columns = new DataColumn[2];
		columns[0] = new DataColumn("a", StringValue.class);

		row.setValue(columns[0], new StringValue("test1"));
		assertEquals(row.getValue(columns[0]).getValue(), "test1");
	}

	@Test
	public void testSetExistingValue() throws Exception {
		DataRow row = new DataRow();
		DataColumn[] columns = new DataColumn[2];
		columns[0] = new DataColumn("a", StringValue.class);

		row.setValue(columns[0], new StringValue("test1"));
		assertEquals(row.getValue(columns[0]).getValue(), "test1");

		row.setValue(columns[0], new StringValue("test2"));
		assertEquals(row.getValue(columns[0]).getValue(), "test2");
	}

	@Test
	public void testGetNotExistingValue() throws Exception {
		DataRow row = new DataRow();
		DataColumn column = new DataColumn("a", StringValue.class);

		assertEquals(row.getValue(column), null);

	}

}