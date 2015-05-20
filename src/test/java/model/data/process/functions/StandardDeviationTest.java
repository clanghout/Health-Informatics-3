package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.process.functions.StandardDeviation;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;
import static org.junit.Assert.*;

/**
 * Test for Standard Deviation.
 * 
 * @author Louis Gosschalk 12-05-2015
 */
public class StandardDeviationTest {

	private DataTable table;
	private DataColumn stringColumn;
	private DataColumn intColumn;
	private DataColumn intsColumn;
	private DataColumn floatColumn;
	private DataColumn floatsColumn;
	private DataColumn floatersColumn;

	/**
	 * simulate datamodel.
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

		StringValue string = new StringValue("What");
		FloatValue int1 = new FloatValue(9);
		FloatValue int2 = new FloatValue(12);
		FloatValue float1 = new FloatValue(6.9f);
		FloatValue float2 = new FloatValue(8.8f);
		FloatValue float3 = new FloatValue(6.6f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		string = new StringValue("Can");
		int1 = new FloatValue(5);
		int2 = new FloatValue(10);
		float1 = new FloatValue(6.5f);
		float2 = new FloatValue(6.9f);
		float3 = new FloatValue(6.6f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		string = new StringValue("You");
		int1 = new FloatValue(3);
		int2 = new FloatValue(3);
		float1 = new FloatValue(5.9f);
		float2 = new FloatValue(8.8f);
		float3 = new FloatValue(6.4f);
		builder.createRow(string, int1, int2, float1, float2, float3);

		string = new StringValue("Do");
		int1 = new FloatValue(10);
		int2 = new FloatValue(12);
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
	public void TestStringStandardDeviation() throws Exception {
		DataValue StandardDeviation = new StandardDeviation(table, new RowValueDescriber<>(stringColumn)).calculate();
	}

	@Test
	public void testFloatStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(floatColumn)).calculate();
		assertEquals(new FloatValue(0.36996624f), std);
	}

	@Test
	public void testIntStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(intColumn)).calculate();
		assertEquals(new FloatValue(2.8613808f), std);
	}

	@Test
	public void testFloatMultipleStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(floatsColumn)).calculate();
		FloatValue f = new FloatValue(1.4637281f);
		assertEquals(f, std);
	}

	@Test
	public void testFloatTripleStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(floatersColumn)).calculate();
		FloatValue f = new FloatValue(0.08660246f);
		assertEquals(f, std);
	}

	@Test
	public void testIntMultipleStandardDeviation() throws Exception {
		DataValue std = new StandardDeviation(table, new RowValueDescriber<>(intsColumn)).calculate();
		FloatValue f = new FloatValue(3.6996622f);
		assertEquals(f, std);
	}
}
