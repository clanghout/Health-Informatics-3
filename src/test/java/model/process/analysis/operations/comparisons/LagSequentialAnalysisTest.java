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
public class LagSequentialAnalysisTest {

	private Event event;
	private Event event2;
	private Event event3;
	private DataTable tablecheck;
	private RowValueDescriber<DataValue> dateCol;
	private RowValueDescriber<DataValue> dateCol2;

	/**
	 * simulate two events.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("Testing Table One Leet H@x0R");
		
		

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
}
