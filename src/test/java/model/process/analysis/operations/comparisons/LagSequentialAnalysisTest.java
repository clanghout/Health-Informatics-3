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
	private DataColumn dateCol;
	private DataColumn DateCol2;

	/**
	 * simulate datamodel with single maximum for each column type.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		builder.createColumn("date", DateTimeValue.class);
		builder.createColumn("string", StringValue.class);
		builder.createColumn("measurement", IntValue.class);

		DateTimeValue date = new DateTimeValue(19, 1, 1994, 11, 30, 5);
		StringValue string = new StringValue("One");
		IntValue inty = new IntValue(12);
		builder.createRow(date, string, inty);

		DataTable table = builder.build();

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("test2");
		builder2.createColumn("date", DateTimeValue.class);
		builder2.createColumn("string", StringValue.class);
		builder2.createColumn("measurement", IntValue.class);
		
		date = new DateTimeValue(19, 1, 1994, 11, 30, 5);
		string = new StringValue("One");
		inty = new IntValue(12);
		builder2.createRow(date, string, inty);
		
		date = new DateTimeValue(10, 1, 1997, 15, 20, 5);
		string = new StringValue("Two");
		inty = new IntValue(9);
		builder2.createRow(date, string, inty);

		date = new DateTimeValue(9, 11, 2001, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder2.createRow(date, string, inty);

		DataTable table2 = builder2.build();
		
		DataDescriber<BoolValue> greater = 
				new ConstraintDescriber(
						new GreaterThanCheck<>(
								new RowValueDescriber<>(table.getColumn("measurement")),
								new ConstantDescriber<>(new IntValue(0))
						)
				);
		event = new Event(table, greater);
		
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
	 * Initialize an event (all measurements above 10).
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEvent() throws Exception {
		
	}
}
