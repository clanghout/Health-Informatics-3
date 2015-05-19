package model.data.process.functions;

import java.util.List;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.Maximum;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;
import static org.junit.Assert.*;

/**
 * @author Louis Gosschalk
 * 12-05-2015
 * Test for maximum
 */
public class MaximumTest {
	
	private DataTable table;
	private DataColumn stringColumn;
	private DataColumn intColumn;
	private DataColumn intsColumn;
	private DataColumn floatColumn;
	private DataColumn floatsColumn;
	private DataColumn floatersColumn;
	
	/**
	 * simulate datamodel with single maximum for each column type
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", IntValue.class);
		intsColumn = builder.createColumn("ints", IntValue.class);
		floatColumn = builder.createColumn("float", FloatValue.class);
		floatsColumn = builder.createColumn("floats", FloatValue.class);
		floatersColumn = builder.createColumn("floaters", FloatValue.class);

		builder.addColumn(stringColumn);
		builder.addColumn(intColumn);
		builder.addColumn(intsColumn);
		builder.addColumn(floatColumn);
		builder.addColumn(floatsColumn);
		builder.addColumn(floatersColumn);
		
		builder.addRow(builder.createRow(new StringValue("What"), new IntValue(9), new IntValue(12), new FloatValue(6.9f), new FloatValue(8.8f), new FloatValue(6.6f)));
		builder.addRow(builder.createRow(new StringValue("Can"), new IntValue(5), new IntValue(10), new FloatValue(6.5f), new FloatValue(6.9f), new FloatValue(6.6f)));
		builder.addRow(builder.createRow(new StringValue("You"), new IntValue(3), new IntValue(3), new FloatValue(5.9f), new FloatValue(8.8f), new FloatValue(6.4f)));
		builder.addRow(builder.createRow(new StringValue("Do"), new IntValue(10), new IntValue(12), new FloatValue(6.2f), new FloatValue(5.3f), new FloatValue(6.6f)));
		
		table = builder.build();
	}
	/**
	 * column of strings should throw exception
	 * @throws Exception
	 */
	@Test(expected=FunctionInputMismatchException.class)
	public void TestStringMaximum() throws Exception {
		List<DataRow> maximum = new Maximum(table, new RowValueDescriber<>(stringColumn)).calculate();
	}
	@Test
	public void testFloatMaximum() throws Exception {
		List<DataRow> max = new Maximum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.9f), max.get(0).getValue(floatColumn));
	}
	@Test
	public void testIntMaximum() throws Exception {
		List<DataRow> max = new Maximum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new IntValue(10), max.get(0).getValue(intColumn));
	}
	@Test
	public void testFloatMultipleMaximum() throws Exception {
		List<DataRow> max = new Maximum(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(8.8f);
		assertEquals(f, max.get(0).getValue(floatsColumn));
		assertEquals(f, max.get(1).getValue(floatsColumn));
	}
	@Test
	public void testFloatTripleMaximum() throws Exception {
		List<DataRow> max = new Maximum(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, max.get(0).getValue(floatersColumn));
		assertEquals(f, max.get(1).getValue(floatersColumn));
		assertEquals(f, max.get(2).getValue(floatersColumn));
	}
	@Test
	public void testIntMultipleMaximum() throws Exception {
		List<DataRow> max = new Maximum(table, new RowValueDescriber<>(intsColumn)).calculate();
		IntValue f = new IntValue(12);
		assertEquals(f, max.get(0).getValue(intsColumn));
		assertEquals(f, max.get(1).getValue(intsColumn));
	}
}
