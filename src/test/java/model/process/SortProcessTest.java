package model.process;

import static org.junit.Assert.*;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.value.*;
import model.process.SortProcess;
import model.process.SortProcess.Order;

import org.junit.Before;
import org.junit.Test;

public class SortProcessTest {
	private DataTable table;
	private DataColumn datetimecol;
	private DataColumn datecol;
	private DataColumn timecol;
	private DataColumn periodcol;
	private DataColumn stringcol;
	private DataColumn boolcol;
	private DataColumn floatcol;
	private DataColumn intcol;
	private Order asc;
	private Order desc;
	
	
	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("Test Data Set");
		
		datetimecol = builder.createColumn("datetime", DateTimeValue.class);
		datecol = builder.createColumn("date", DateValue.class);
		timecol = builder.createColumn("time", TimeValue.class);
		periodcol = builder.createColumn("period", PeriodValue.class);
		stringcol = builder.createColumn("string", StringValue.class);
		boolcol = builder.createColumn("boolean", BoolValue.class);
		floatcol = builder.createColumn("float", FloatValue.class);
		intcol = builder.createColumn("int", IntValue.class);
		
		DataValue datetime = new DateTimeValue(2015, 1, 19, 11, 30, 10);
		DataValue date = new DateValue(2015, 1, 19);
		DataValue time = new TimeValue(11, 30, 10);
		DataValue period = new PeriodValue(3, 4, 20);
		DataValue string = new StringValue("klm");
		DataValue bool = new BoolValue(true);
		DataValue floot = new FloatValue(6f);
		DataValue indt = new IntValue(7);
		
		builder.createRow(datetime, date, time, period, string, bool, floot, indt);
		
		date = new DateTimeValue(1994, 1, 19, 11, 30, 10);
		date = new DateValue(1994, 1, 19);
		time = new TimeValue(11, 20, 9);
		period = new PeriodValue(0, 9, 10);
		string = new StringValue("abc");
		bool = new BoolValue(false);
		floot = new FloatValue(5.9f);
		indt = new IntValue(3);
		
		builder.createRow(datetime, date, time, period, string, bool, floot, indt);
		
		date = new DateTimeValue(2019, 1, 19, 11, 30, 10);
		date = new DateValue(2019, 1, 19);
		time = new TimeValue(11, 20, 10);
		period = new PeriodValue(10, 8, 10);
		string = new StringValue("abb");
		bool = new BoolValue(true);
		floot = new FloatValue(10f);
		indt = new IntValue(8);
		
		builder.createRow(datetime, date, time, period, string, bool, floot, indt);
		
		date = new DateTimeValue(2015, 1, 20, 11, 30, 10);
		date = new DateValue(2015, 1, 20);
		time = new TimeValue(16, 30, 10);
		period = new PeriodValue(0, 9, 6);
		string = new StringValue("opq");
		bool = new BoolValue(false);
		floot = new FloatValue(2f);
		indt = new IntValue(7);
		
		builder.createRow(datetime, date, time, period, string, bool, floot, indt);
		
		table = builder.build();
		
		asc = SortProcess.Order.ASCENDING;
		desc = SortProcess.Order.DESCENDING;
	}
	public DataTable proc(DataProcess process) {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		process.setInput(table);
		return (DataTable) process.process();
	}
	
	@Test
	public void testSortingDateTime() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(datetimecol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("klm"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingDate() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(datecol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingTime() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(timecol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingPeriod() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(periodcol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingString() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(stringcol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abb"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingBool() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(boolcol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingFloat() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(floatcol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingInt() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(intcol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	/**
	 * REVERSE, REVERSE!
	 * https://youtu.be/wZv62ShoStY?t=3m17s
	 */
	
	@Test
	public void testReverseSortingDateTime() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(datetimecol), desc);
		DataTable result = proc(process);
		assertEquals(new StringValue("klm"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingDate() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(datecol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingTime() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(timecol), asc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingPeriod() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(periodcol), desc);
		DataTable result = proc(process);
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingString() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(stringcol), desc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abb"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingBool() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(boolcol), desc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingFloat() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(floatcol), desc);
		DataTable result = proc(process);
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingInt() throws Exception {
		DataProcess process = new SortProcess(new RowValueDescriber<>(intcol), desc);
		DataTable result = proc(process);
		assertEquals(new StringValue("abb"), result.getRow(0).getValue(stringcol));
	}
}
