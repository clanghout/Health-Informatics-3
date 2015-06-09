package model.process.setOperations;

import model.data.*;
import model.data.value.FloatValue;
import model.data.value.NumberValue;
import model.data.value.StringValue;
import model.language.Identifier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/28/15.
 */
public class UnionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalUnion() throws Exception {
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

		DataModel model = new DataModel();
		model.add(table1);
		model.add(table2);

		Union union = new Union(new Identifier<>("table1"), new Identifier<>("table2"));
		union.setDataModel(model);

		union.process();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalUnion2() throws Exception {
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
		DataModel model = new DataModel();
		model.add(table1);
		model.add(table2);

		Union union = new Union(new Identifier<>("table1"), new Identifier<>("table2"));
		union.setDataModel(model);

		union.process();
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
		DataModel model = new DataModel();
		model.add(table1);
		model.add(table2);

		Union union = new Union(new Identifier<>("table1"), new Identifier<>("table2"));
		union.setDataModel(model);

		DataTable table3 = union.doProcess();

		assertEquals(table3.getRowCount(), 2);
		assertEquals(table2.getRowCount(), 1);
		assertEquals(table1.getRowCount(), 1);
		assertEquals(table3.getRow(0).getValue(table3.getColumn("c1")).getValue(), "test");
		assertEquals(table3.getRow(1).getValue(table3.getColumn("c1")).getValue(), "test2");
	}

	@Test
	public void testDoProcessUnionSameRow() throws Exception {
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
		DataModel model = new DataModel();
		model.add(table1);
		model.add(table2);

		Union union = new Union(new Identifier<>("table1"), new Identifier<>("table2"));
		union.setDataModel(model);

		DataTable table3 = union.doProcess();

		assertEquals(table3.getRowCount(), 1);
		assertEquals(table3.getRow(0).getValue(table3.getColumn("c1")).getValue(), "test");
	}

	@Test
	public void testDoProcessUnionSameRowMultipleCodes() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		DataRow row1 = builder.createRow(new StringValue("test"));
		row1.addCode("test");
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);
		DataRow row2 = builder2.createRow(new StringValue("test"));
		row2.addCode("test2");


		DataTable table2 = builder2.build();
		DataModel model = new DataModel();
		model.add(table1);
		model.add(table2);

		Union union = new Union(new Identifier<>("table1"), new Identifier<>("table2"));
		union.setDataModel(model);

		DataTable table3 = union.doProcess();

		assertEquals(table3.getRowCount(), 1);
		assertEquals(table3.getRow(0).getValue(table3.getColumn("c1")).getValue(), "test");
		assertTrue(table3.getRow(0).containsCode("test"));
		assertTrue(table3.getRow(0).containsCode("test2"));

		assertTrue(table1.getRow(0).containsCode("test"));
		assertFalse(table1.getRow(0).containsCode("test2"));

		assertFalse(table2.getRow(0).containsCode("test"));
		assertTrue(table2.getRow(0).containsCode("test2"));
	}

	@Test
	public void testDoProcessUnionMultipleRowsAndColumns() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.createColumn("c1", StringValue.class);
		builder.createColumn("c2", FloatValue.class);
		builder.createRow(new StringValue("tesawfawt"), new FloatValue(22));
		DataRow row1 = builder.createRow(new StringValue("test"), new FloatValue(2));
		builder.createRow(new StringValue("tesawfawawdt"), new FloatValue(222));
		row1.addCode("test");
		builder.setName("table1");

		DataTable table1 = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("table2");
		builder2.createColumn("c1", StringValue.class);
		builder2.createColumn("c2", FloatValue.class);
		builder2.createRow(new StringValue("eg"), new FloatValue(2));
		builder2.createRow(new StringValue("test"), new FloatValue(5));
		DataRow row2 = builder2.createRow(new StringValue("test"), new FloatValue(2));
		row2.addCode("test2");
		DataRow row3 = builder2.createRow(new StringValue("tesaawt"), new FloatValue(22));
		row3.addCode("te2");


		DataTable table2 = builder2.build();
		DataModel model = new DataModel();
		model.add(table1);
		model.add(table2);

		Union union = new Union(new Identifier<>("table1"), new Identifier<>("table2"));
		union.setDataModel(model);

		DataTable table3 = union.doProcess();

		assertEquals(table3.getRowCount(), 6);
		assertEquals(table3.getRow(0).getValue(table3.getColumn("c1")).getValue(), "tesawfawt");
		assertEquals(table3.getRow(0).getValue(table3.getColumn("c2")), new FloatValue(22));
		assertFalse(table3.getRow(0).containsCode("test"));
		assertFalse(table3.getRow(0).containsCode("test2"));


		assertEquals(table3.getRow(1).getValue(table3.getColumn("c1")).getValue(), "test");
		assertEquals(table3.getRow(1).getValue(table3.getColumn("c2")),	new FloatValue(2));
		assertTrue(table3.getRow(1).containsCode("test"));
		assertTrue(table3.getRow(1).containsCode("test2"));

		assertEquals(table3.getRow(3).getValue(table3.getColumn("c1")).getValue(), "eg");
		assertEquals(table3.getRow(3).getValue(table3.getColumn("c2")),	new FloatValue(2));

	}
}