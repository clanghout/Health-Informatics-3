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
import model.data.value.DataValue;
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
public class LagSequentialTest {

	private Event event;
	private Event event2;
	private Event event3;
	private DataTable tablecheck;
	private DataColumn datecol;
	private DataColumn datecol2;
	private RowValueDescriber<DataValue> datedesc;
	private RowValueDescriber<DataValue> datedesc2;
	
	/**
	 * simulate two events.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUpEvent1() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("Table 1");

		datecol = builder.createColumn("date", DateTimeValue.class);
		builder.createColumn("measurement", IntValue.class);
		
		datedesc = new RowValueDescriber<>(datecol);
		
		DataValue date = new DateTimeValue(2015, 1, 19, 10, 30, 30);
		DataValue inti = new IntValue(43);
		builder.createRow(date, inti);

		date = new DateTimeValue(2019, 1, 19, 10, 30, 30);
		inti = new IntValue(23);
		builder.createRow(date, inti);

		DataTable table = builder.build();

		DataDescriber<BoolValue> greater = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table.getColumn("measurement")),
						new ConstantDescriber<>(new IntValue(0))));

		event = new Event(table, greater);

	}

	@Before
	public void setUpEvent2() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("Table 2");

		datecol2 = builder.createColumn("date2", DateTimeValue.class);
		builder.createColumn("measurement2", IntValue.class);
		
		datedesc2 = new RowValueDescriber<>(datecol2);

		DataValue date = new DateTimeValue(2016, 1, 19, 10, 30, 30);
		DataValue inti = new IntValue(21);
		builder.createRow(date, inti);

		date = new DateTimeValue(2020, 1, 19, 10, 30, 30);
		inti = new IntValue(90);
		builder.createRow(date, inti);

		DataTable table = builder.build();

		DataDescriber<BoolValue> greater = new ConstraintDescriber(
				new GreaterThanCheck<>(new RowValueDescriber<>(
						table.getColumn("measurement2")),
						new ConstantDescriber<>(new IntValue(0))));

		event2 = new Event(table, greater);
	}

	/**
	 * Call LSA on the created events.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEvent() throws Exception {
		datedesc = new RowValueDescriber<>(datecol);
		datedesc2 = new RowValueDescriber<>(datecol2);
//		RowValueDescriber<DataValue> datedesc = new RowValueDescriber<DataValue>(
//				datecol);
//		RowValueDescriber<DataValue> datedesc2 = new RowValueDescriber<DataValue>(
//				datecol2);

		LagSequential lsa = new LagSequential(event, datedesc, event2,
				datedesc2);
		DataTable result = lsa.getResult();
		assertEquals(((DataTable) event.create()).getRowCount(), 2);
		assertEquals(((DataTable) event2.create()).getRowCount(), 2);
		assertEquals(lsa.getTableInputOne().getRowCount(), 2);
		assertEquals(lsa.getTableInputTwo().getRowCount(), 2);
		
		assertEquals(result.getRowCount(),4);
	}

	/**
	 * Call LSA on empty table event.
	 * 
	 * @throws Exception
	 */
//	@Test(expected = InputMismatchException.class)
//	public void testEmptyTable() throws Exception {
//		LagSequential lsa = new LagSequential(event3, dateCol, event2, dateCol2);
//	}
}
