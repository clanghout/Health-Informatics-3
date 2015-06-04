package model.language;

import model.data.*;
import model.data.value.BoolValue;
import model.data.value.IntValue;
import model.data.value.StringValue;
import model.process.DataProcess;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

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

		Table test2 = model.getByName("test2").get();
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
		assertEquals(new IntValue(10), row.getValue(table.getColumn("value")));
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
	public void testParseFromMultiple() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();

		builder.setName("test2");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(21));
		builder.createRow(new IntValue(25));

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "from(test1, test2)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof CombinedDataTable);

		CombinedDataTable table = (CombinedDataTable) result;
		DataColumn test1Column = test1.getColumn("value");
		DataColumn test2Column = test2.getColumn("value");

		Iterator<? extends Row> rows = table.iterator();

		Row row = rows.next();
		assertEquals(new IntValue(11), row.getValue(test1Column));
		assertEquals(new IntValue(21), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(11), row.getValue(test1Column));
		assertEquals(new IntValue(25), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(10), row.getValue(test1Column));
		assertEquals(new IntValue(21), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(10), row.getValue(test1Column));
		assertEquals(new IntValue(25), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(9), row.getValue(test1Column));
		assertEquals(new IntValue(21), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(9), row.getValue(test1Column));
		assertEquals(new IntValue(25), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(5), row.getValue(test1Column));
		assertEquals(new IntValue(21), row.getValue(test2Column));

		row = rows.next();
		assertEquals(new IntValue(5), row.getValue(test1Column));
		assertEquals(new IntValue(25), row.getValue(test2Column));

		assertFalse(rows.hasNext());
	}

	@Test
	public void testReferFutureTable() throws Exception {
		String input = "def gtTen() : Constraint = (test2.value > 10);" +
				"from(test1)|is(test2)|from(test2)|constraint(gtTen)|is(result)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(1, table.getRowCount());

		DataRow row = table.getRow(0);

		assertEquals(new IntValue(11), row.getValue(table.getColumn("value")));
	}

	@Test
	public void testBooleanTableValue() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");

		builder.createColumn("value", BoolValue.class);

		builder.createRow(new BoolValue(true));
		builder.createRow(new BoolValue(false));

		model.add(builder.build());

		String input = "def isTrue() : Constraint = test2.value;" +
				"from(test2)|constraint(isTrue)|is(result)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(1, table.getRowCount());

		DataRow row = table.getRow(0);

		assertEquals(new BoolValue(true), row.getValue(table.getColumn("value")));
	}

	@Test
	public void testCodeCheck() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");

		builder.createColumn("value", IntValue.class);
		builder.createColumn("code", StringValue.class);

		builder.createRow(new IntValue(11), new StringValue("appel"));
		builder.createRow(new IntValue(9), new StringValue("test"));
		builder.createRow(new IntValue(8), new StringValue("somethingelse"));
		builder.createRow(new IntValue(7), new StringValue("sjon"));

		DataTable test2 = builder.build();

		test2.getRow(0).addCode("appel");
		test2.getRow(2).addCode("test");

		model.add(test2);

		String input =
				"def codeCheck() : Constraint = HAS_CODE(\"test\") OR HAS_CODE(test2.code);" +
				"from(test2)|constraint(codeCheck)|is(result)";

		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(2, table.getRowCount());

		DataRow row1 = table.getRow(0);
		DataRow row3 = table.getRow(1);

		assertTrue(row1.getCodes().contains("appel"));
		assertEquals(new IntValue(11), row1.getValue(table.getColumn("value")));

		assertTrue(row3.getCodes().contains("test"));
		assertEquals(new IntValue(8), row3.getValue(table.getColumn("value")));
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