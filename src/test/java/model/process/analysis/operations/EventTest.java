package model.process.analysis.operations;

import static org.junit.Assert.*;

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
import model.process.analysis.operations.constraints.GreaterThanCheck;

import org.junit.Before;
import org.junit.Test;

/**
 * This test creates an event.
 *
 * @author Louis Gosschalk 28-05-2015
 */
public class EventTest {

	private DataTable table;
	private DataTable table2;
	private DataColumn stringColumn;
	private DataColumn dateColumn;
	private DataColumn measureColumn;
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
		dateColumn = builder.createColumn("date", DateTimeValue.class);
		stringColumn = builder.createColumn("string", StringValue.class);
		measureColumn = builder.createColumn("measurement", IntValue.class);

		DateTimeValue date = new DateTimeValue(19, 1, 1994, 11, 30, 5);
		StringValue string = new StringValue("One");
		IntValue inty = new IntValue(12);
		builder.createRow(date, string, inty);

		table = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test");
		dateColumn = builder.createColumn("date", DateTimeValue.class);
		stringColumn = builder.createColumn("string", StringValue.class);
		measureColumn = builder.createColumn("measurement", IntValue.class);

		date = new DateTimeValue(19, 1, 1994, 11, 30, 5);
		string = new StringValue("One");
		inty = new IntValue(12);
		builder.createRow(date, string, inty);
		
		date = new DateTimeValue(10, 1, 1997, 15, 20, 5);
		string = new StringValue("Two");
		inty = new IntValue(9);
		builder.createRow(date, string, inty);

		date = new DateTimeValue(9, 11, 2001, 14, 46, 28);
		string = new StringValue("Three");
		inty = new IntValue(10);
		builder.createRow(date, string, inty);

		table2 = builder.build();
	}

	/**
	 * Initialize an event (all measurements above 10).
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEvent() throws Exception {
		assertFalse(table.equals(table2));
		assertTrue(table.getRowCount() > 0);
		assertTrue(table2.getRowCount() > 0);
		DataDescriber<BoolValue> greater = 
				new ConstraintDescriber(
						new GreaterThanCheck<>(
								new RowValueDescriber<>(table.getColumn("measurement")),
								new ConstantDescriber<>(new IntValue(10))));
		Event event = new Event(table2, greater);
		DataTable tableResult = event.create();
		assertTrue(table.equals(tableResult));
	}
}
