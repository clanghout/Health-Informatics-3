package model.data.describer;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataModelBuilder;
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

	private DataModel model;

	private DataColumn stringColumn;
	private DataColumn intColumn;

	@Before
	public void setUp() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();

		stringColumn = builder.createColumn("string", StringValue.class);
		intColumn = builder.createColumn("int", IntValue.class);

		builder.addColumn(stringColumn);
		builder.addColumn(intColumn);

		builder.addRow(builder.createRow(new StringValue("Hello"), new IntValue(5)));

		model = builder.build();
	}

	@Test
	public void testResolve() throws Exception {
		RowValueDescriber<StringValue> describer = new RowValueDescriber<>(stringColumn);
		assertEquals(new StringValue("Hello"), describer.resolve(model.getRow(0)));
	}
}