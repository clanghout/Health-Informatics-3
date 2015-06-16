package model.process.analysis.operations.comparisons;

import static org.junit.Assert.*;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.IntValue;
import model.language.Identifier;

import org.junit.Before;
import org.junit.Test;

/**
 * This test creates an event.
 *
 * @author Louis Gosschalk 28-05-2015
 */
public class LagSequentialTest {

	private DataTable table1;
	private DataTable table2;
	private DataTable table3;
	private DataColumn datecol;
	private DataColumn datecol2;
	private DataColumn datecol3;
	private Identifier<DataColumn> datedesc;
	private Identifier<DataColumn> datedesc2;
	private Identifier<DataColumn> datedesc3;

	/**
	 * simulate two events.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUpEvent1() {
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

		table1 = builder.build();

		model.add(table1);

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

		table2 = builder.build();

		model.add(table2);

		/**
		 * Build third table (empty)
		 */

		builder = new DataTableBuilder();
		builder.setName("Table 2");

		datecol3 = builder.createColumn("date3", DateTimeValue.class);
		builder.createColumn("measurement3", IntValue.class);

		datedesc3 = new Identifier<DataColumn>(datecol3.getName());

		table3 = builder.build();

		model.add(table3);
	}

	/**
	 * Call LSA on the created events.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLSA() throws Exception {
		LagSequential lsa = new LagSequential(table1, datedesc, table2,
				datedesc2, "lsa");
		DataTable result = lsa.getResult();
		assertEquals(table1.getRowCount(), 2);
		assertEquals(table2.getRowCount(), 2);

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

	/**
	 * Call LSA on empty table event.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NullPointerException.class)
	public void testEmptyTable() throws Exception {
		LagSequential lsa = new LagSequential(table3, datedesc3, table2,
				datedesc2, "lsa");
	}
}
