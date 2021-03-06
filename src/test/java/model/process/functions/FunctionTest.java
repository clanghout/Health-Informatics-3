package model.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DateTimeValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;
import org.junit.Before;

/**
 * This test framework provides a datamodel for a test to use.
 * @author Louis Gosschalk 20-05-2015
 *
 */
public abstract class FunctionTest {
	
	protected DataTable table;
	protected DataColumn stringColumn;
	protected DataColumn intColumn;
	protected DataColumn intsColumn;
	protected DataColumn floatColumn;
	protected DataColumn floatsColumn;
	protected DataColumn floatersColumn;
	protected DataColumn dateColumn;
	private DataTableBuilder builder;
	
	/**
	 * simulate datamodel with single maximum for each column type.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() {
		builder = new DataTableBuilder();
		builder.setName("test");

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", IntValue.class);
		intsColumn = builder.createColumn("ints", IntValue.class);
		floatColumn = builder.createColumn("float", FloatValue.class);
		floatsColumn = builder.createColumn("floats", FloatValue.class);
		floatersColumn = builder.createColumn("floaters", FloatValue.class);
		dateColumn = builder.createColumn("date", DateTimeValue.class);

		StringValue string = new StringValue("What");
		IntValue int1 = new IntValue(9);
		IntValue int2 = new IntValue(12);
		FloatValue float1 = new FloatValue(6.9f);
		FloatValue float2 = new FloatValue(8.8f);
		FloatValue float3 = new FloatValue(6.6f);
		DateTimeValue dateTimeValue = new DateTimeValue(1995, 1, 17, 0, 0, 0);
		builder.createRow(string, int1, int2, float1, float2, float3, dateTimeValue);

		string = new StringValue("Can");
		int1 = new IntValue(5);
		int2 = new IntValue(10);
		float1 = new FloatValue(6.5f);
		float2 = new FloatValue(6.9f);
		float3 = new FloatValue(6.6f);
		dateTimeValue = new DateTimeValue(1996, 1, 17, 0, 0, 0);
		builder.createRow(string, int1, int2, float1, float2, float3, dateTimeValue);

		string = new StringValue("You");
		int1 = new IntValue(3);
		int2 = new IntValue(3);
		float1 = new FloatValue(5.9f);
		float2 = new FloatValue(8.8f);
		float3 = new FloatValue(6.4f);
		dateTimeValue = new DateTimeValue(1997, 1, 17, 0, 0, 0);
		builder.createRow(string, int1, int2, float1, float2, float3, dateTimeValue);

		string = new StringValue("Do");
		int1 = new IntValue(10);
		int2 = new IntValue(12);
		float1 = new FloatValue(6.2f);
		float2 = new FloatValue(5.3f);
		float3 = new FloatValue(6.6f);
		dateTimeValue = new DateTimeValue(1998, 1, 17, 0, 0, 0);
		builder.createRow(string, int1, int2, float1, float2, float3, dateTimeValue);



		table = builder.build();
	}
	
	public void extendTable() {
		StringValue string = new StringValue("Bruh");
		IntValue int1 = new IntValue(11);
		IntValue int2 = new IntValue(53);
		FloatValue float1 = new FloatValue(9.2f);
		FloatValue float2 = new FloatValue(4.8f);
		FloatValue float3 = new FloatValue(7.0f);
		DateTimeValue dateTimeValue = new DateTimeValue(1999, 1, 17, 0, 0, 0);
		builder.createRow(string, int1, int2, float1, float2, float3, dateTimeValue);

		table = builder.build();
	}
}
