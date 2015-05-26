package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 4/29/15.
 */
public class DataRowTest {

	private DataColumn[] columns1;
	private DataColumn[] columns2;
	private DataValue[] values1;
	private DataValue[] values2;

	@Before
	public void setUp() throws Exception {
		columns1 = new DataColumn[3];
		columns1[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns1[1] = new DataColumn("b", new DataTable(), StringValue.class);
		columns1[2] = new DataColumn("c", new DataTable(), DataValue.class);

		columns2 = new DataColumn[3];
		columns2[0] = new DataColumn("a", new DataTable(), StringValue.class);
		columns2[1] = new DataColumn("b", new DataTable(), StringValue.class);
		columns2[2] = new DataColumn("c", new DataTable(), DataValue.class);

		values1 = new DataValue[3];
		values1[0] = new StringValue("test1");
		values1[1] = new StringValue("test2");
		values1[2] = new StringValue("test3");

		values2 = new DataValue[3];
		values2[0] = new StringValue("test1");
		values2[1] = new StringValue("test2");
		values2[2] = new StringValue("test3");
	}


	@Test
	public void testConstructor() throws Exception {
		DataRow row = new DataRow(columns1, values1);

		assertEquals(row.getValue(columns1[0]).getValue(), "test1");
		assertEquals(row.getValue(columns1[1]).getValue(), "test2");
		assertEquals(row.getValue(columns1[2]).getValue(), "test3");
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testConstructorMismatchNotEnoughValues() throws Exception {
		DataValue[] values = new DataValue[2];

		values[0] = new StringValue("test1");
		values[1] = new StringValue("test2");
		DataRow row = new DataRow(columns1, values);
	}

	@Test(expected = ColumnValueMismatchException.class)
	public void testConstructorMismatchNotEnoughColumns() throws Exception {
		DataColumn[] columns = new DataColumn[2];
		columns[0] = new DataColumn("a", null, StringValue.class);
		columns[1] = new DataColumn("b", null, StringValue.class);

		DataRow row = new DataRow(columns, values1);
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
		DataRow row = new DataRow(columns1, values1);

		assertTrue(row.hasColumn(columns1[2]));
		assertTrue(row.hasColumn(columns1[1]));
		assertTrue(row.hasColumn(columns1[0]));
	}

	@Test
	public void testHasColumnFalse() throws Exception {
		DataColumn column = new DataColumn("no", null, DataValue.class);

		DataRow row = new DataRow(columns1, values1);

		assertFalse(row.hasColumn(column));
	}

	@Test
	public void testCopy() throws Exception {
		DataRow row = new DataRow(columns1, values1);
		DataRow copy = row.copy();
		assertEquals(copy.getValue(columns1[0]), row.getValue(columns1[0]));
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
		DataRow row = new DataRow(columns1, values1);
		DataRow copy = row.copy();
		assertTrue(row.equals(copy));
	}

	@Test
	public void testEqualsFalse() throws Exception {
		DataRow row = new DataRow(columns1, values1);
		DataRow copy = new DataRow(columns2, values2);
		assertFalse(row.equals(copy));
	}

	@Test
	public void testSoftEqual() throws Exception {
		DataRow row = new DataRow(columns1, values1);
		DataRow copy = new DataRow(columns2, values2);
		assertTrue(row.equalsSoft(copy));
	}

	@Test
	public void testHasCode() throws Exception {
		DataRow row = new DataRow(columns1, values1);
		DataRow copy = new DataRow(columns2, values2);
		assertEquals(row.hashCode(), copy.hashCode());
	}



}