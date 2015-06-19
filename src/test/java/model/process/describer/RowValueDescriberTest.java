package model.process.describer;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.IntValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test for the RowValueDescriber.
 * Created by Boudewijn on 11-5-2015.
 */
public class RowValueDescriberTest {

	private DataTable table;

	private DataColumn stringColumn;
	private DataColumn intColumn;

	@Before
	public void setUp() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", IntValue.class);


		builder.createRow(new StringValue("Hello"), new IntValue(5));

		table = builder.build();
	}

	@Test
	public void testResolve() throws Exception {
		RowValueDescriber<StringValue> describer = new RowValueDescriber<>(stringColumn);
		assertEquals(new StringValue("Hello"), describer.resolve(table.getRow(0)));
	}
}