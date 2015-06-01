package model.process.analysis.operations;

import static org.junit.Assert.*;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.DateTimeValue;
import model.data.value.StringValue;
import model.process.analysis.operations.constraints.GreaterThanCheck;

import org.junit.Before;
import org.junit.Test;

/**
 * This test creates an event.
 * 
 * @author Louis Gosschalk 28-05-2015
 */
public abstract class EventTest {

	private DataTable table;
	private DataColumn stringColumn;
	private DataColumn dateColumn;
	private DataTableBuilder builder;

	/**
	 * simulate datamodel with single maximum for each column type.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() {
		builder = new DataTableBuilder();
		dateColumn = builder.createColumn("date", DateTimeValue.class);
		stringColumn = builder.createColumn("string", StringValue.class);
		DateTimeValue date = new DateTimeValue(19, 1, 1994, 11, 30, 5);
		StringValue string = new StringValue("One");
		builder.createRow(date, string);

		table = builder.build();
	}

	/**
	 * All instances of an event after a certain date.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEvent() throws Exception {
		assertEquals(1, 1);
		DataDescriber<BoolValue> greater = new ConstraintDescriber(
				new GreaterThanCheck<>(
						new RowValueDescriber<>(table.getColumn("date")), 
						new ConstantDescriber(new DateTimeValue(19, 1, 1993, 11, 30, 5))
						));
		Event event = new Event(table, greater);
		Table tableResult = event.create();
		assertEquals(table, tableResult);
	}
}
