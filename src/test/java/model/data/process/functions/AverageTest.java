package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
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
 * Test for average
 */
public class AverageTest {
	
	private DataTable table;
	private DataColumn stringColumn;
	private DataColumn intColumn;
	private DataColumn intsColumn;
	private DataColumn floatColumn;
	private DataColumn floatsColumn;
	
	/**
	 * simulate datamodel with single average for each column type
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		DataTableBuilder builder = new DataTableBuilder("test");

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", IntValue.class);
		intsColumn = builder.createColumn("ints", IntValue.class);
		floatColumn = builder.createColumn("float", FloatValue.class);
		floatsColumn = builder.createColumn("floats", FloatValue.class);

		builder.addColumn(stringColumn);
		builder.addColumn(intColumn);
		builder.addColumn(intsColumn);
		builder.addColumn(floatColumn);
		builder.addColumn(floatsColumn);
		
		builder.addRow(builder.createRow(new StringValue("What"), new IntValue(9), new IntValue(12), new FloatValue(6.9f), new FloatValue(8.8f)));
		builder.addRow(builder.createRow(new StringValue("Can"), new IntValue(5), new IntValue(10), new FloatValue(6.5f), new FloatValue(6.9f)));
		builder.addRow(builder.createRow(new StringValue("You"), new IntValue(3), new IntValue(3), new FloatValue(5.9f), new FloatValue(8.8f)));
		builder.addRow(builder.createRow(new StringValue("Do"), new IntValue(10), new IntValue(12), new FloatValue(6.2f), new FloatValue(5.3f)));
		
		table = builder.build();
	}
	/**
	 * column of strings should throw exception
	 * @throws Exception
	 */
	@Test(expected=FunctionInputMismatchException.class)
	public void TestStringMinimum() throws Exception {
		DataValue minimum = new Average(table, new RowValueDescriber<>(stringColumn)).calculate();
	}
	@Test
	public void testFloatAverage() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.375f), av);
	}
	@Test
	public void testIntAverage() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(6.75f), av);
	}
	@Test
	public void testFloatAverage2() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(floatsColumn)).calculate();
		assertEquals(new FloatValue(7.45f), av);
	}
	@Test
	public void testIntAverage2() throws Exception {
		DataValue av = new Average(table, new RowValueDescriber<>(intsColumn)).calculate();
		assertEquals(new FloatValue(9.25f), av);
	}
}
