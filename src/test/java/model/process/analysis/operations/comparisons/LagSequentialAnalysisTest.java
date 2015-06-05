package model.process.analysis.operations.comparisons;

import static org.junit.Assert.*;
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
import model.process.analysis.operations.Event;
import model.process.analysis.operations.constraints.GreaterThanCheck;

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
	private DataDescriber<DateTimeValue> dateCol;
	private DataDescriber<DateTimeValue> dateCol2;

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
		
		dateCol = new RowValueDescriber<DateTimeValue>(dat);
		
		DataTable table = builder.build();

		DataDescriber<BoolValue> greater = 
				new ConstraintDescriber(
						new GreaterThanCheck<>(
								new RowValueDescriber<>(table.getColumn("measurement")),
								new ConstantDescriber<>(new IntValue(0))
						)
				);
		event = new Event(table, greater);
		
		
		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("test2");
		dat = builder2.createColumn("date", DateTimeValue.class);
		builder2.createColumn("string", StringValue.class);
		builder2.createColumn("measurement", IntValue.class);
		
		date = new DateTimeValue(2014, 1, 19, 11, 30, 5);
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
		
		DataDescriber<BoolValue> greater2 = 
				new ConstraintDescriber(
						new GreaterThanCheck<>(
								new RowValueDescriber<>(table2.getColumn("measurement")),
								new ConstantDescriber<>(new IntValue(0))
						)
				);
		event2 = new Event(table2, greater2);
		
	}

	/**
	 * Call LSA on the created events.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEvent() throws Exception {
		LagSequentialAnalysis lsa = new LagSequentialAnalysis(event, dateCol, event2, dateCol2);
	}
}
