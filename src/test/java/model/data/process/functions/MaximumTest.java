package model.data.process.functions;

import java.util.List;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.Maximum;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;
import static org.junit.Assert.*;

/**
 * Test for maximum.
 * 
 * @author Louis Gosschalk 12-05-2015
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
	 * simulate datamodel with single maximum for each column type.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", FloatValue.class);
		intsColumn = builder.createColumn("ints", FloatValue.class);
		floatColumn = builder.createColumn("float", FloatValue.class);
		floatsColumn = builder.createColumn("floats", FloatValue.class);
		floatersColumn = builder.createColumn("floaters", FloatValue.class);

		builder.addColumn(stringColumn);
		builder.addColumn(intColumn);
		builder.addColumn(intsColumn);
		builder.addColumn(floatColumn);
		builder.addColumn(floatsColumn);
		builder.addColumn(floatersColumn);

		StringValue string = new StringValue("What");
		FloatValue int1 = new FloatValue(9);
		FloatValue int2 = new FloatValue(12);
		FloatValue float1 = new FloatValue(6.9f);
		FloatValue float2 = new FloatValue(8.8f);
		FloatValue float3 = new FloatValue(6.6f);
		builder.addRow(builder.createRow(string, int1, int2, float1, float2, float3));

		string = new StringValue("Can");
		int1 = new FloatValue(5);
		int2 = new FloatValue(10);
		float1 = new FloatValue(6.5f);
		float2 = new FloatValue(6.9f);
		float3 = new FloatValue(6.6f);
		builder.addRow(builder.createRow(string, int1, int2, float1, float2, float3));

		string = new StringValue("You");
		int1 = new FloatValue(3);
		int2 = new FloatValue(3);
		float1 = new FloatValue(5.9f);
		float2 = new FloatValue(8.8f);
		float3 = new FloatValue(6.4f);
		builder.addRow(builder.createRow(string, int1, int2, float1, float2, float3));

		string = new StringValue("Do");
		int1 = new FloatValue(10);
		int2 = new FloatValue(12);
		float1 = new FloatValue(6.2f);
		float2 = new FloatValue(5.3f);
		float3 = new FloatValue(6.6f);
		builder.addRow(builder.createRow(string, int1, int2, float1, float2, float3));

		table = builder.build();
	}

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringMaximum() throws Exception {
		DataValue maximum = new Maximum(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.9f), max);
	}

	@Test
	public void testIntMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(10.0f), max);
	}

	@Test
	public void testFloatMultipleMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(8.8f);
		assertEquals(f, max);
	}

	@Test
	public void testFloatTripleMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, max);
	}

	@Test
	public void testIntMultipleMaximum() throws Exception {
		DataValue max = new Maximum(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(12.0f);
		assertEquals(f, max);
	}
}
