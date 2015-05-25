package language;

import model.data.*;
import model.data.process.DataProcess;
import model.data.value.IntValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The tests for the Parser.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class ParserTest {

	@Test
	public void testParseFrom() throws Exception {
		String input = "from(test1)";

		DataModel model = new DataModel();
		DataTable test1 = new DataTable("test1");
		model.add(test1);

		Parser parser = new Parser();
		DataProcess process = parser.parse(input, model);

		Table result = process.process();

		assertEquals(test1, result);
	}

	@Test
	public void testParseFromIs() throws Exception {
		String input = "from(test1)|is(test2)";

		DataModel model = new DataModel();
		DataTable test1 = new DataTable("test1");
		model.add(test1);

		Parser parser = new Parser();
		DataProcess process = parser.parse(input, model);

		Table result = process.process();
		assertEquals(test1, result);

		Table test2 = model.getByName("test2");
		assertTrue(test1.equalsSoft(test2));
	}

	@Test
	public void testParseFromConstraint() throws Exception {
		String input = "def isTen() : Constraint = test1.value = 10;\n" +
				"from(test1)|constraint(isTen)";

		DataModel model = new DataModel();

		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(10));
		builder.createRow(new IntValue(5));

		DataTable inputTable = builder.build();
		model.add(inputTable);

		Parser parser = new Parser();
		DataProcess process = parser.parse(input, model);

		Table result = process.process();

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(1, table.getRowCount());
		DataRow row = table.getRow(0);
		// TODO: make table.getColumn("value") work here as well.
		assertEquals(new IntValue(10), row.getValue(inputTable.getColumn("value")));
	}
}