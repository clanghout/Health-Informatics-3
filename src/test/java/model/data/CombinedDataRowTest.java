package model.data;

import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/19/15.
 */
public class CombinedDataRowTest {

	@Test
	public void testAddDataRow() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", null, StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));

		combRow.addDataRow(dataRow);
		assertEquals(combRow.getValue(column1).toString(), "test");
	}

	@Test
	public void testGetValue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", new DataTable(), StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));

		DataColumn column2 = new DataColumn("test1", new DataTable(), StringValue.class);
		DataRow dataRow2 = new DataRow();
		dataRow.setValue(column2, new StringValue("test2"));

		combRow.addDataRow(dataRow);
		combRow.addDataRow(dataRow2);
		assertEquals(combRow.getValue(column1).toString(), "test");
		assertEquals(combRow.getValue(column2).toString(), "test2");
	}

	@Test
	public void testhasColumnTrue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		DataColumn column1 = new DataColumn("test1", null, StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));
		assertTrue(dataRow.hasColumn(column1));

		DataColumn column2 = new DataColumn("test2", null, StringValue.class);
		DataRow dataRow2 = new DataRow();
		dataRow2.setValue(column2, new StringValue("test"));
		assertTrue(dataRow2.hasColumn(column2));

		combRow.addDataRow(dataRow);
		combRow.addDataRow(dataRow2);
		assertTrue(combRow.hasColumn(column1));
		assertTrue(combRow.hasColumn(column2));
	}

	@Test
	public void testhasColumnFalse() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", new DataTable(), StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));

		DataColumn column2 = new DataColumn("test1", new DataTable(), StringValue.class);
		DataRow dataRow2 = new DataRow();
		dataRow2.setValue(column2, new StringValue("test2"));

		combRow.addDataRow(dataRow);

		assertFalse(combRow.hasColumn(column2));
	}


	@Test
	public void testSetValue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", new DataTable(), StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));

		DataColumn column2 = new DataColumn("test1", new DataTable(), StringValue.class);
		DataRow dataRow2 = new DataRow();
		dataRow.setValue(column2, new StringValue("test2"));

		combRow.addDataRow(dataRow);
		combRow.addDataRow(dataRow2);
		assertEquals(combRow.getValue(column1).toString(), "test");
		assertEquals(combRow.getValue(column2).toString(), "test2");

		combRow.setValue(column1, new StringValue("test3"));
		assertEquals(combRow.getValue(column1).toString(), "test3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetValueException() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", null, StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));
		combRow.addDataRow(dataRow);

		combRow.getValue(new DataColumn("test2", null, StringValue.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetValueException() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", null, StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));
		combRow.addDataRow(dataRow);

		combRow.setValue(new DataColumn("test2", null, StringValue.class), new StringValue("test2"));
	}

	@Test
	public void testGetRows() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		DataColumn column1 = new DataColumn("test1", null, StringValue.class);
		DataRow dataRow = new DataRow();
		dataRow.setValue(column1, new StringValue("test"));

		DataColumn column2 = new DataColumn("test1", null, StringValue.class);
		DataRow dataRow2 = new DataRow();
		dataRow.setValue(column2, new StringValue("test2"));

		combRow.addDataRow(dataRow);
		combRow.addDataRow(dataRow2);

		assertEquals(combRow.getRows().size(), 2);
		assertTrue(combRow.getRows().contains(dataRow));
		assertTrue(combRow.getRows().contains(dataRow2));
	}

}