package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.Median;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;

import static org.junit.Assert.*;

/**
 * Test for Median.
 * 
 * @author Louis Gosschalk 16-05-2015
 */
public class MedianTest {

	private DataTable table;
	private DataColumn stringColumn;
	private DataColumn intColumn;
	private DataColumn intsColumn;
	private DataColumn floatColumn;
	private DataColumn floatsColumn;
	private DataColumn floatersColumn;
	private DataTableBuilder builder;

	/**
	 * simulate datamodel with single Median for each column type.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		builder = new DataTableBuilder();

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", IntValue.class);
		intsColumn = builder.createColumn("ints", IntValue.class);
		floatColumn = builder.createColumn("float", FloatValue.class);
		floatsColumn = builder.createColumn("floats", FloatValue.class);
		floatersColumn = builder.createColumn("floaters", FloatValue.class);

		StringValue string = new StringValue("What");
		IntValue int1 = new IntValue(9);
		IntValue int2 = new IntValue(12);
		FloatValue float1 = new FloatValue(6.9f);
		FloatValue float2 = new FloatValue(8.8f);
		FloatValue float3 = new FloatValue(6.6f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		string = new StringValue("Can");
		int1 = new IntValue(5);
		int2 = new IntValue(10);
		float1 = new FloatValue(6.5f);
		float2 = new FloatValue(6.9f);
		float3 = new FloatValue(6.6f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		string = new StringValue("You");
		int1 = new IntValue(3);
		int2 = new IntValue(3);
		float1 = new FloatValue(5.9f);
		float2 = new FloatValue(8.8f);
		float3 = new FloatValue(6.4f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		string = new StringValue("Do");
		int1 = new IntValue(10);
		int2 = new IntValue(12);
		float1 = new FloatValue(6.2f);
		float2 = new FloatValue(5.3f);
		float3 = new FloatValue(6.6f);
		builder.createRow(string, int1, int2, float1, float2, float3);
		builder.setName("test");
		table = builder.build();
	}

	/**
	 * column of strings should throw exception.
	 * 
	 * @throws Exception
	 */
	@Test(expected = FunctionInputMismatchException.class)
	public void TestStringMedian() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatMedian() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.35f), med);
	}

	@Test
	public void testIntMedian() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(7.0f), med);
	}

	@Test
	public void testFloatMedian2() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(7.8500004f);
		assertEquals(f, med);
	}

	@Test
	public void testIntMedian2() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(11.0f);
		assertEquals(f, med);
	}

	@Test
	public void testMedianTriplet() throws Exception {
		DataValue med = new Median(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, med);
	}

	@Test
	public void testMedianOdd() throws Exception {
		StringValue string = new StringValue("What");
		IntValue int1 = new IntValue(11);
		IntValue int2 = new IntValue(53);
		FloatValue float1 = new FloatValue(9.2f);
		FloatValue float2 = new FloatValue(4.8f);
		FloatValue float3 = new FloatValue(7.0f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		table = builder.build();

		DataValue med = new Median(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(6.5f), med);

		med = new Median(table, new RowValueDescriber<>(floatsColumn)).calculate();
		assertEquals(new FloatValue(6.9f), med);

		med = new Median(table, new RowValueDescriber<>(floatersColumn)).calculate();
		assertEquals(new FloatValue(6.6f), med);

		med = new Median(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(9.0f), med);

		med = new Median(table, new RowValueDescriber<>(intsColumn)).calculate();
		assertEquals(new FloatValue(12.0f), med);
	}

}
