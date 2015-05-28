package model.data;

import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/19/15.
 */
public class CombinedDataRowTest {


	private DataColumn column1;
	private DataColumn column2;
	private DataRow row1;
	private DataRow row2;

	@Before
	public void setUp() throws Exception {
		DataTable table = new DataTable("table");
		column1 = new DataColumn("test1", table, StringValue.class);
		column2 = new DataColumn("test2", table, StringValue.class);

		row1 = new DataRow();
		row1.setValue(column1, new StringValue("test"));

		row2 = new DataRow();
		row2.setValue(column2, new StringValue("test2"));

	}

	@Test
	public void testAddDataRow() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		combRow.addDataRow(row1);
		assertEquals(combRow.getValue(column1).toString(), "test");
	}

	@Test
	public void testGetValue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		assertEquals(combRow.getValue(column1).toString(), "test");
		assertEquals(combRow.getValue(column2).toString(), "test2");
	}

	@Test
	public void testGetValueSameColumnName() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		combRow.addDataRow(row1);
		combRow.addDataRow(row2);

		DataColumn column3 = new DataColumn("test1", new DataTable("table2"), StringValue.class);
		DataRow row3 = new DataRow();
		row3.setValue(column3, new StringValue("test3"));

		combRow.addDataRow(row3);
		assertEquals(combRow.getValue(column1).toString(), "test");
		assertEquals(combRow.getValue(column2).toString(), "test2");
		assertEquals(combRow.getValue(column3).toString(), "test3");

	}

	@Test
	public void testhasColumnTrue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();


		assertTrue(row2.hasColumn(column2));

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		assertTrue(combRow.hasColumn(column1));
		assertTrue(combRow.hasColumn(column2));
	}

	@Test
	public void testhasColumnFalse() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		combRow.addDataRow(row1);

		assertFalse(combRow.hasColumn(column2));
	}


	@Test
	public void testSetValue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		assertEquals(combRow.getValue(column1).toString(), "test");
		assertEquals(combRow.getValue(column2).toString(), "test2");

		combRow.setValue(column1, new StringValue("test3"));
		assertEquals(combRow.getValue(column1).toString(), "test3");
	}

	@Test
	public void testSetValueSameColumnName() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		combRow.addDataRow(row1);
		combRow.addDataRow(row2);

		DataColumn column3 = new DataColumn("test1", new DataTable("table2"), StringValue.class);
		DataRow row3 = new DataRow();
		row3.setValue(column3, new StringValue("test3"));

		combRow.addDataRow(row3);
		assertEquals(combRow.getValue(column1).toString(), "test");
		assertEquals(combRow.getValue(column2).toString(), "test2");
		assertEquals(combRow.getValue(column3).toString(), "test3");

		combRow.setValue(column1, new StringValue("test5"));
		assertEquals(combRow.getValue(column1).toString(), "test5");
		assertEquals(combRow.getValue(column2).toString(), "test2");
		assertEquals(combRow.getValue(column3).toString(), "test3");

		combRow.setValue(column3, new StringValue("test6"));
		assertEquals(combRow.getValue(column1).toString(), "test5");
		assertEquals(combRow.getValue(column2).toString(), "test2");
		assertEquals(combRow.getValue(column3).toString(), "test6");

	}


	@Test(expected = IllegalArgumentException.class)
	public void testGetValueException() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		combRow.addDataRow(row1);

		combRow.getValue(new DataColumn("test2", null, StringValue.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetValueException() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
		combRow.addDataRow(row1);

		combRow.setValue(new DataColumn("test2", null, StringValue.class), new StringValue("test2"));
	}

	@Test
	public void testGetRows() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);

		assertEquals(combRow.getRows().size(), 2);
		assertTrue(combRow.getRows().contains(row1));
		assertTrue(combRow.getRows().contains(row2));
	}

	@Test
	public void testCopy() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);

		assertEquals(combRow, combRow.copy());
	}

	@Test
	public void testEqualsTrue() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		CombinedDataRow copy = new CombinedDataRow();
		copy.addDataRow(row1);
		copy.addDataRow(row2);

		assertTrue(combRow.equals(copy));
	}

	@Test
	public void testEqualsFalse() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		CombinedDataRow copy = new CombinedDataRow();
		copy.addDataRow(row1);

		assertFalse(combRow.equals(copy));
	}

	@Test
	public void testEqualsSoft() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
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

		builder.build();
		builder2.build();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		CombinedDataRow copy = new CombinedDataRow();
		copy.addDataRow(row1C);
		copy.addDataRow(row2C);
		assertFalse(combRow.equals(copy));
		assertTrue(combRow.equalsSoft(copy));
	}

	@Test
	public void testHashCode() throws Exception {
		CombinedDataRow combRow = new CombinedDataRow();
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

		builder.build();
		builder2.build();

		combRow.addDataRow(row1);
		combRow.addDataRow(row2);
		CombinedDataRow copy = new CombinedDataRow();
		copy.addDataRow(row1C);
		copy.addDataRow(row2C);

		assertEquals(combRow.hashCode(), copy.hashCode());
	}

	@Test
	public void testGetCodesNowRows() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		assertTrue(comb.getCodes().size() == 0);
	}

	@Test
	public void testGetCodesOneRow() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		comb.addDataRow(row1);
		assertTrue(comb.getCodes().size() == 0);

		row1.addCode("test");
		row1.addCode("test2");

		assertTrue(comb.getCodes().size() == 2);
		assertTrue(comb.getCodes().contains("test"));
		assertTrue(comb.getCodes().contains("test2"));
	}

	@Test
	public void testGetCodesMultipleRows() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		comb.addDataRow(row1);
		comb.addDataRow(row2);
		assertTrue(comb.getCodes().size() == 0);

		row1.addCode("test");
		row1.addCode("test2");
		row2.addCode("test3");

		assertTrue(comb.getCodes().size() == 3);
		assertTrue(comb.getCodes().contains("test"));
		assertTrue(comb.getCodes().contains("test2"));
		assertTrue(comb.getCodes().contains("test3"));
	}

	@Test
	public void testGetCodesMultipleRowsSameCode() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		comb.addDataRow(row1);
		comb.addDataRow(row2);
		assertTrue(comb.getCodes().size() == 0);

		row1.addCode("test");
		row1.addCode("test2");
		row2.addCode("test2");

		assertTrue(comb.getCodes().size() == 2);
		assertTrue(comb.getCodes().contains("test"));
		assertTrue(comb.getCodes().contains("test2"));
	}




	@Test
	public void testContainsCodeEmpty() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		assertFalse(comb.containsCode("test"));
	}

	@Test
	public void testContainsCodeTrue() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		comb.addDataRow(row1);
		comb.addDataRow(row2);
		row1.addCode("test1");
		row2.addCode("test2");
		row1.addCode("test3");
		assertTrue(comb.containsCode("test1"));
		assertTrue(comb.containsCode("test2"));
		assertTrue(comb.containsCode("test3"));
	}

	@Test
	public void testContainsCodeFalse() throws Exception {
		CombinedDataRow comb = new CombinedDataRow();
		comb.addDataRow(row1);
		comb.addDataRow(row2);
		row1.addCode("test");
		row1.addCode("test2");
		row2.addCode("test3");
		assertFalse(comb.containsCode("test5"));

	}

}