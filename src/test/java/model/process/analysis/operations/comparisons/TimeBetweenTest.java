package model.process.analysis.operations.comparisons;

import static org.junit.Assert.*;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.PeriodValue;
import model.data.value.TimeValue;
import model.language.Identifier;
import model.process.DataProcess;

import model.process.analysis.TimeBetween;
import org.junit.Before;
import org.junit.Test;

public class TimeBetweenTest {
	private DataTable table;

	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test table");

		builder.createColumn("date time", DateTimeValue.class);
		builder.createColumn("date", DateValue.class);
		builder.createColumn("time", TimeValue.class);

		DataValue datetime = new DateTimeValue(2014, 6, 20, 10, 20, 10);
		DataValue date = new DateValue(2019, 6, 9);
		DataValue time = new TimeValue(21, 12, 50);
		builder.createRow(datetime, date, time);

		datetime = new DateTimeValue(2016, 1, 19, 10, 10, 10);
		date = new DateValue(2019, 7, 4);
		time = new TimeValue(20, 8, 29);
		builder.createRow(datetime, date, time);

		table = builder.build();

	}

	@Test
	public void testDateTime() {
		Identifier<DataColumn> idDateTime = new Identifier<DataColumn>(
				"date time");

		DataProcess tb = new TimeBetween(idDateTime);
		tb.setInput(table);

		DataTable result = (DataTable) tb.process();

		assertEquals(result.getRowCount(), 2);

		DataValue compareDate = new PeriodValue(0, 0, 0);
		DataValue dateDiff = result.getRow(0).getValue(
				result.getColumn("Difference date"));
		DataValue compareTime = new TimeValue(0, 0, 0);
		DataValue timeDiff = result.getRow(0).getValue(
				result.getColumn("Difference time"));

		assertEquals(compareDate, dateDiff);
		assertEquals(compareTime, timeDiff);

		compareDate = new PeriodValue(1, 6, 29);
		dateDiff = result.getRow(1).getValue(
				result.getColumn("Difference date"));
		compareTime = new TimeValue(23, 50, 0);
		timeDiff = result.getRow(1).getValue(
				result.getColumn("Difference time"));

		assertEquals(compareDate, dateDiff);
		assertEquals(compareTime, timeDiff);
	}

	@Test
	public void testDate() {
		Identifier<DataColumn> idDate = new Identifier<DataColumn>("date");

		DataProcess tb = new TimeBetween(idDate);
		tb.setInput(table);

		DataTable result = (DataTable) tb.process();

		assertEquals(result.getRowCount(), 2);

		DataValue compareDate = new PeriodValue(0, 0, 0);
		DataValue dateDiff = result.getRow(0).getValue(
				result.getColumn("Difference date"));

		assertEquals(compareDate, dateDiff);

		compareDate = new PeriodValue(0, 0, 25);
		dateDiff = result.getRow(1).getValue(
				result.getColumn("Difference date"));

		assertEquals(compareDate, dateDiff);
	}

	@Test
	public void testTime() {
		Identifier<DataColumn> idTime = new Identifier<DataColumn>("time");

		DataProcess tb = new TimeBetween(idTime);
		tb.setInput(table);

		DataTable result = (DataTable) tb.process();

		assertEquals(result.getRowCount(), 2);

		DataValue compareTime = new TimeValue(0, 0, 0);
		DataValue timeDiff = result.getRow(0).getValue(
				result.getColumn("Difference time"));

		assertEquals(compareTime, timeDiff);

		compareTime = new TimeValue(22, 55, 39);
		timeDiff = result.getRow(1).getValue(
				result.getColumn("Difference time"));

		assertEquals(compareTime, timeDiff);
	}
}
