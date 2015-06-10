package model.process;

import static org.junit.Assert.*;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.PeriodValue;
import model.data.value.StringValue;
import model.process.SortProcess;

import org.junit.Before;
import org.junit.Test;

public class SortProcessTest {
	private DataTable table;
	private DataColumn datecol;
	private DataColumn periodcol;
	private DataColumn stringcol;
	private DataColumn boolcol;
	private DataColumn floatcol;
	private DataColumn intcol;
	
	
	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("Test Data Set");
		
		datecol = builder.createColumn("date", DateTimeValue.class);
		periodcol = builder.createColumn("period", PeriodValue.class);
		stringcol = builder.createColumn("string", StringValue.class);
		boolcol = builder.createColumn("boolean", BoolValue.class);
		floatcol = builder.createColumn("float", FloatValue.class);
		intcol = builder.createColumn("int", IntValue.class);
		
		DataValue date = new DateTimeValue(2015, 1, 19, 11, 30, 10);
		DataValue period = new PeriodValue(1, 4, 20);
		DataValue string = new StringValue("klm");
		DataValue bool = new BoolValue(true);
		DataValue floot = new FloatValue(6f);
		DataValue indt = new IntValue(7);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		date = new DateTimeValue(1994, 1, 19, 11, 30, 10);
		period = new PeriodValue(10, 9, 10);
		string = new StringValue("abc");
		bool = new BoolValue(false);
		floot = new FloatValue(5.9f);
		indt = new IntValue(3);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		date = new DateTimeValue(2019, 1, 19, 11, 30, 10);
		period = new PeriodValue(10, 8, 10);
		string = new StringValue("xyz");
		bool = new BoolValue(true);
		floot = new FloatValue(10f);
		indt = new IntValue(8);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		date = new DateTimeValue(2015, 1, 20, 11, 30, 10);
		period = new PeriodValue(10, 9, 10);
		string = new StringValue("abc");
		bool = new BoolValue(false);
		floot = new FloatValue(2f);
		indt = new IntValue(7);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		table = builder.build();
	}
	
	@Test
	public void testSorting() throws Exception {
		DataProcess process = new SortProcess(table, new RowValueDescriber<>(datecol));
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals("abc", table.getRow(0).getValue(stringcol));
	}
}
