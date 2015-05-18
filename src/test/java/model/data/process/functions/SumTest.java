package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.Sum;
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
 * 16-05-2015
 * Test for sum
 */
public class SumTest {
	
	private DataTable table;
	private DataColumn stringColumn;
	private DataColumn intColumn;
	private DataColumn intsColumn;
	private DataColumn floatColumn;
	private DataColumn floatsColumn;
	private DataColumn floatersColumn;
	
	/**
	 * simulate datamodel with single sum for each column type
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();

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
	public void TestStringsum() throws Exception {
		DataValue sum = new Sum(table, new RowValueDescriber<>(stringColumn)).calculate();
	}
	@Test
	public void testFloatsum() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(25.5f), sm);
	}
	@Test
	public void testIntsum() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(27), sm);
	}
	@Test
	public void testFloatSum2() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(29.8f);
		assertEquals(f, sm);
	}
	@Test
	public void testFloatSum3() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(26.2f);
		assertEquals(f, sm);
	}
	@Test
	public void testIntSum2() throws Exception {
		DataValue sm = new Sum(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(37.0f);
		assertEquals(f, sm);
	}
}
