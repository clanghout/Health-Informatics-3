package model.data;

import model.data.value.IntValue;
import model.data.value.StringValue;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by jens on 4/29/15.
 */
public class DataColumnTest {

	@Test
	 public void testGetName() throws Exception {
		DataColumn column = new DataColumn("test", null, StringValue.class);
		assertEquals(column.getName(), "test");
	}

	@Test
	public void testGetType() throws Exception {
		DataColumn column = new DataColumn("test", null, StringValue.class);
		assertEquals(column.getType(), StringValue.class);
	}

	@Test
	public void testGetTable() throws Exception {
		DataTable table1 = new DataTable();
		DataColumn column = new DataColumn("test", table1, StringValue.class);
		assertEquals(column.getTable(), table1);
	}

	@Test
	public void testSetTable() throws Exception {
		DataTable table1 = new DataTable();
		DataColumn column = new DataColumn("test", null, StringValue.class);
		assertEquals(column.getTable(), null);
		column.setTable(table1);
		assertEquals(column.getTable(), table1);
	}

	@Test
	public void testCopy() throws Exception {
		DataTable table1 = new DataTable();
		DataColumn column = new DataColumn("test", table1, StringValue.class);
		DataColumn copy = column.copy();
		assertEquals(column.getTable(), copy.getTable());
		assertEquals(column.getName(), copy.getName());
		assertEquals(column.getType(), copy.getType());
	}

	@Test
	public void testEqualsTrue() throws Exception {
		DataTable table1 = new DataTable();
		DataColumn column = new DataColumn("test", table1, StringValue.class);
		DataColumn column2 = new DataColumn("test", table1, StringValue.class);
		assertTrue(column.equals(column2));
	}

	@Test
	public void testEqualsFalse() throws Exception {
		DataTable table1 = new DataTable();
		DataColumn column = new DataColumn("test2", table1, StringValue.class);
		DataColumn column2 = new DataColumn("test", table1, StringValue.class);
		DataColumn column3 = new DataColumn("test", table1, IntValue.class);

		assertFalse(column.equals(column2));
		assertFalse(column.equals(column3));
	}

	@Test
	public void testHasCode() throws Exception {
		DataTable table1 = new DataTable();
		DataColumn column = new DataColumn("test", table1, StringValue.class);
		DataColumn column2 = new DataColumn("test", table1, StringValue.class);

		assertEquals(column.hashCode(), column2.hashCode());
	}
}