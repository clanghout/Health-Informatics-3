package model.process;

import static org.junit.Assert.*;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.PeriodValue;
import model.data.value.StringValue;
import model.process.SortProcess;
import model.process.SortProcess.Order;

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
	private Order asc;
	private Order desc;
	
	
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
		DataValue period = new PeriodValue(3, 4, 20);
		DataValue string = new StringValue("klm");
		DataValue bool = new BoolValue(true);
		DataValue floot = new FloatValue(6f);
		DataValue indt = new IntValue(7);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		date = new DateTimeValue(1994, 1, 19, 11, 30, 10);
		period = new PeriodValue(0, 9, 10);
		string = new StringValue("abc");
		bool = new BoolValue(false);
		floot = new FloatValue(5.9f);
		indt = new IntValue(3);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		date = new DateTimeValue(2019, 1, 19, 11, 30, 10);
		period = new PeriodValue(10, 8, 10);
		string = new StringValue("abb");
		bool = new BoolValue(true);
		floot = new FloatValue(10f);
		indt = new IntValue(8);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		date = new DateTimeValue(2015, 1, 20, 11, 30, 10);
		period = new PeriodValue(0, 9, 6);
		string = new StringValue("opq");
		bool = new BoolValue(false);
		floot = new FloatValue(2f);
		indt = new IntValue(7);
		
		builder.createRow(date, period, string, bool, floot, indt);
		
		table = builder.build();
		
		asc = SortProcess.Order.ASCENDING;
		desc = SortProcess.Order.ASCENDING;
	}
	
	@Test
	public void testSortingDate() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(datecol), asc);
		process.setInput(table);
		DataTable result = (DataTable) process.process();
//		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingPeriod() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(periodcol), asc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingString() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(stringcol), asc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abb"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingBool() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(boolcol), asc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingFloat() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(floatcol), asc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testSortingInt() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(intcol), asc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	/**
	 * REVERSE, REVERSE!
	 * https://youtu.be/wZv62ShoStY?t=3m17s
	 */
	
	@Test
	public void testReverseSortingDate() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(datecol), desc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingPeriod() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(periodcol), desc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingString() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(stringcol), desc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abb"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingBool() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(boolcol), desc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingFloat() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(floatcol), desc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("opq"), result.getRow(0).getValue(stringcol));
	}
	
	@Test
	public void testReverseSortingInt() throws Exception {
		assertEquals(new StringValue("klm"), table.getRow(0).getValue(stringcol));
		DataProcess process = new SortProcess(new RowValueDescriber<>(intcol), desc);
		process.setInput(table);
		DataTable result = process.doProcess().getTable(table.getName());
		assertEquals(new StringValue("abc"), result.getRow(0).getValue(stringcol));
	}
}
