package model.process.analysis.operations.comparisons;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.DateTimeValue;
import model.data.value.IntValue;
import model.data.value.StringValue;
import model.exceptions.InputMismatchException;
import model.process.analysis.operations.Event;
import model.process.analysis.operations.constraints.GreaterThanCheck;
import model.process.functions.StandardDeviation;

import org.junit.Before;
import org.junit.Test;

/**
 * This test creates an event.
 *
 * @author Louis Gosschalk 28-05-2015
 */
public class LagSequentialAnalysisTest {

	private Event event;
	private Event event2;
	private Event event3;
	private Event event4;
	private Event event5;
	private DataTable tablecheck;
	private DataDescriber<DateTimeValue> dateCol;
	private DataDescriber<DateTimeValue> dateCol2;
	private DataDescriber<DateTimeValue> dateCol4;
	private DataDescriber<DateTimeValue> dateCol5;

	/**
	 * simulate two events.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() {

		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		DataColumn dat = builder.createColumn("date", DateTimeValue.class);
		builder.createColumn("string", StringValue.class);
		builder.createColumn("measurement", IntValue.class);

		DateTimeValue date = new DateTimeValue(2015, 1, 19, 11, 30, 9);
		StringValue string = new StringValue("One");
		IntValue inty = new IntValue(12);
		builder.createRow(date, string, inty);

		date = new DateTimeValue(2014, 1, 19, 11, 30, 9);
		string = new StringValue("One");
		inty = new IntValue(12);
		builder.createRow(date, string, inty);

		date = new DateTimeValue(1994, 1, 19, 11, 30, 9);
		string = new StringValue("One");
		inty = new IntValue(12);
		builder.createRow(date, string, inty);

		date = new DateTimeValue(2016, 1, 19, 11, 30, 9);
		string = new StringValue("One");
		inty = new IntValue(12);
		builder.createRow(date, string, inty);

		dateCol = new RowValueDescriber<DateTimeValue>(dat);

		DataTable table = builder.build();

		DataDescriber<BoolValue> greater = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));
		event = new Event(table, greater);

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("test2");
		dat = builder2.createColumn("date", DateTimeValue.class);
		builder2.createColumn("string", StringValue.class);
		builder2.createColumn("measurement", IntValue.class);

		date = new DateTimeValue(2015, 1, 19, 11, 30, 5);
		string = new StringValue("One");
		inty = new IntValue(12);
		builder2.createRow(date, string, inty);

		date = new DateTimeValue(2014, 1, 20, 15, 20, 5);
		string = new StringValue("Two");
		inty = new IntValue(9);
		builder2.createRow(date, string, inty);

		date = new DateTimeValue(2015, 11, 9, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder2.createRow(date, string, inty);

		dateCol2 = new RowValueDescriber<DateTimeValue>(dat);

		DataTable table2 = builder2.build();

		DataDescriber<BoolValue> greater2 = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table2.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));
		event2 = new Event(table2, greater2);

		DataTableBuilder builder3 = new DataTableBuilder();
		builder3.setName("EMPTYTABLETEST");
		dat = builder3.createColumn("date", DateTimeValue.class);
		builder3.createColumn("string", StringValue.class);
		builder3.createColumn("measurement", IntValue.class);

		DataTable table3 = builder3.build();

		DataDescriber<BoolValue> greater3 = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table3.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));
		event3 = new Event(table3, greater3);
		
		DataTableBuilder builder4 = new DataTableBuilder();
		builder4.setName("Tablecheck");
		dat = builder4.createColumn("date", DateTimeValue.class);
		builder4.createColumn("string", StringValue.class);
		builder4.createColumn("measurement", IntValue.class);
		
		dateCol4 = new RowValueDescriber<DateTimeValue>(dat);
		
		date = new DateTimeValue(2015, 11, 9, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder4.createRow(date, string, inty);
		
		DataTable table4 = builder4.build();
		
		DataDescriber<BoolValue> greater4 = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table4.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));
		event4 = new Event(table4, greater4);
		
		DataTableBuilder builder5 = new DataTableBuilder();
		builder5.setName("Tablecheck2");
		dat = builder5.createColumn("date", DateTimeValue.class);
		builder5.createColumn("string", StringValue.class);
		builder5.createColumn("measurement", IntValue.class);
		
		date = new DateTimeValue(2015, 12, 9, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder5.createRow(date, string, inty);
		
		dateCol5 = new RowValueDescriber<DateTimeValue>(dat);
		
		DataTable table5 = builder5.build();
		
		DataDescriber<BoolValue> greater5 = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table5.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));
		event5 = new Event(table5, greater5);
		
		DataTableBuilder builder6 = new DataTableBuilder();
		builder6.setName("Tablechecker");
		dat = builder6.createColumn("date", DateTimeValue.class);
		builder6.createColumn("string", StringValue.class);
		builder6.createColumn("measurement", IntValue.class);
		
		date = new DateTimeValue(2015, 11, 9, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder6.createRow(date, string, inty);
		
		date = new DateTimeValue(2015, 12, 9, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder6.createRow(date, string, inty);
		
		tablecheck = builder6.build();

	}

	/**
	 * Call LSA on the created events.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEvent() throws Exception {
		LagSequentialAnalysis lsa = new LagSequentialAnalysis(event, dateCol,
				event2, dateCol2);
	}

	/**
	 * Call LSA on empty table event.
	 * 
	 * @throws Exception
	 */
	@Test(expected = InputMismatchException.class)
	public void testEmptyTable() throws Exception {
		LagSequentialAnalysis lsa = new LagSequentialAnalysis(event3, dateCol,
				event2, dateCol2);
	}
	
	@Test
	public void testGetTable() throws Exception {
		LagSequentialAnalysis lsa = new LagSequentialAnalysis(event4, dateCol4,
				event5, dateCol5);
		DataTable result = lsa.getTable();
		assertEquals(tablecheck.getRowCount(), result.getRowCount());
	}
	
	@Test
	public void testGetOrder() throws Exception {
		LagSequentialAnalysis lsa = new LagSequentialAnalysis(event, dateCol,
				event2, dateCol2);
		List<String> order = lsa.getOrder();
		List<String> ordercheck = Arrays.asList("A", "A", "B", "B", "A", "B", "A");
		assertEquals(order,ordercheck);
	}
}
