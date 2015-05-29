package model.process.analysis.operations;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DateTimeValue;
import model.data.value.StringValue;

import org.junit.Before;

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
		builder.setName("test");

		dateColumn = builder.createColumn("date", DateTimeValue.class);
		stringColumn = builder.createColumn("string", StringValue.class);
		
		DateTimeValue date = new DateTimeValue(0, 0, 0, 0, 0, 0);
		StringValue string = new StringValue("One");
		builder.createRow(date, string);

		table = builder.build();
	}
}
