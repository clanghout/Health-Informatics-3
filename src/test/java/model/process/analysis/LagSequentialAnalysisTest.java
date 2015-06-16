package model.process.analysis;

import static org.junit.Assert.*;
import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.IntValue;
import model.language.Identifier;
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
	private DataColumn datecol;
	private DataColumn datecol2;
	private Identifier<DataColumn> datedesc;
	private Identifier<DataColumn> datedesc2;

	/**
	 * simulate two events.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUpEvents() {
		DataModel model = new DataModel();

		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("Table 1");

		datecol = builder.createColumn("date", DateTimeValue.class);
		builder.createColumn("measurement", IntValue.class);

		datedesc = new Identifier<DataColumn>(datecol.getName());

		DataValue date = new DateTimeValue(2015, 1, 19, 10, 30, 30);
		DataValue inti = new IntValue(43);
		builder.createRow(date, inti);

		date = new DateTimeValue(2016, 1, 19, 10, 30, 29);
		inti = new IntValue(23);
		builder.createRow(date, inti);

		DataTable table = builder.build();

		DataDescriber<BoolValue> greater = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));

		event = new Event(table, greater);

		model.add(table);

		/**
		 * Build second table
		 */

		builder = new DataTableBuilder();
		builder.setName("Table 2");

		datecol2 = builder.createColumn("date2", DateTimeValue.class);
		builder.createColumn("measurement2", IntValue.class);

		datedesc2 = new Identifier<DataColumn>(datecol2.getName());

		date = new DateTimeValue(2016, 1, 19, 10, 30, 30);
		inti = new IntValue(21);
		builder.createRow(date, inti);

		date = new DateTimeValue(2020, 1, 19, 10, 30, 30);
		inti = new IntValue(90);
		builder.createRow(date, inti);

		table = builder.build();

		greater = new ConstraintDescriber(new GreaterThanCheck<>(
				new RowValueDescriber<>(table.getColumn("measurement2")),
				new ConstantDescriber<>(new IntValue(0))));

		event2 = new Event(table, greater);

		model.add(table);
	}

	/**
	 * Call LSA on the created events.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLSA() throws Exception {
		DataAnalysis anal = new LagSequentialAnalysis(event, datedesc, event2, datedesc2);
		DataTable result = (DataTable) anal.process();

		assertEquals(result.getRowCount(), 4);
		
		DataColumn newdate = result.getColumns().get(0);
		
		assertEquals(new DateTimeValue(2015, 1, 19, 10, 30, 30),
				result.getRow(0).getValue(newdate));
		
		assertEquals(new DateTimeValue(2016, 1, 19, 10, 30, 29),
				result.getRow(1).getValue(newdate));
		
		assertEquals(new DateTimeValue(2016, 1, 19, 10, 30, 30),
				result.getRow(2).getValue(newdate));
		
		assertEquals(new DateTimeValue(2020, 1, 19, 10, 30, 30),
				result.getRow(3).getValue(newdate));

	}
}
