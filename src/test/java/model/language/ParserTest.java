package model.language;

import model.data.*;
import model.process.DataProcess;
import model.data.value.IntValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The tests for the Parser.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class ParserTest {

	private DataModel model;
	private DataTable test1;


	@Before
	public void setUp() throws Exception {
		model = new DataModel();
		DataTableBuilder builder = new DataTableBuilder();

		builder.setName("test1");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(11));
		builder.createRow(new IntValue(10));
		builder.createRow(new IntValue(9));
		builder.createRow(new IntValue(5));

		test1 = builder.build();
		model.add(test1);

	}

	@Test
	public void testParseFrom() throws Exception {
		String input = "from(test1)";

		Table result = parseAndProcess(input);

		assertTrue(test1.equalsSoft(result));
	}

	@Test
	public void testParseFromIs() throws Exception {
		String input = "from(test1)|is(test2)";

		Table result = parseAndProcess(input);
		assertTrue(test1.equalsSoft(result));

		Table test2 = model.getByName("test2");
		assertTrue(test1.equalsSoft(test2));
	}

	@Test
	public void testParseFromEqualsConstraint() throws Exception {
		String input = "def isTen() : Constraint = test1.value = 10;\n" +
				"from(test1)|constraint(isTen)";


		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(1, table.getRowCount());
		DataRow row = table.getRow(0);
		// TODO: make table.getColumn("value") work here as well.
		assertEquals(new IntValue(10), row.getValue(test1.getColumn("value")));
	}

	private Table parseAndProcess(String input) {
		Parser parser = new Parser();
		DataProcess process = parser.parse(input, model);

		return process.process();
	}

	@Test
	public void testParseFromGreaterConstraint() throws Exception {
		String input = "def gtTen() : Constraint = test1.value > 10;\n" +
				"from(test1)|constraint(gtTen)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(1, table.getRowCount());
		DataRow row = table.getRow(0);

		assertEquals(new IntValue(11), row.getValue(test1.getColumn("value")));
	}

	@Test
	public void testParseFromLesserConstraint() throws Exception {
		String input = "def ltTen() : Constraint = test1.value < 10;\n" +
				"from(test1)|constraint(ltTen)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(2, table.getRowCount());
		DataRow row = table.getRow(0);
		DataRow row2 = table.getRow(1);

		assertEquals(new IntValue(9), row.getValue(test1.getColumn("value")));
		assertEquals(new IntValue(5), row2.getValue(test1.getColumn("value")));
	}

	@Test
	public void testParseFromGreaterEqualsConstraint() throws Exception {
		String input = "def gtEqTen() : Constraint = test1.value >= 10;\n" +
				"from(test1)|constraint(gtEqTen)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(2, table.getRowCount());
		DataRow row = table.getRow(0);
		DataRow row2 = table.getRow(1);

		assertEquals(new IntValue(11), row.getValue(test1.getColumn("value")));
		assertEquals(new IntValue(10), row2.getValue(test1.getColumn("value")));
	}

	@Test
	public void testParseSetCodes() throws Exception {
		String input = "def gtNine() : Constraint = test1.value > 9;\n" +
				"from(test1)|constraint(gtNine)}is(gtThen)|from(test1)|setCode(\"hallo\", gtThen)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertTrue(table.getRow(0).getCodes().contains("hallo"));
		assertTrue(table.getRow(1).getCodes().contains("hallo"));
		assertFalse(table.getRow(2).getCodes().contains("hallo"));
		assertFalse(table.getRow(3).getCodes().contains("hallo"));
	}
}