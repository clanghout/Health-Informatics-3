package model.language;

import model.data.*;
import model.data.value.*;
import model.exceptions.ParseException;
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
	private DateTimeValue firstDateTime;
	private DateTimeValue secondDateTime;


	@Before
	public void setUp() throws Exception {
		model = new DataModel();
		DataTableBuilder builder = new DataTableBuilder();

		builder.setName("test1");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("date", DateTimeValue.class);

		firstDateTime = new DateTimeValue(1995, 1, 17, 3, 45, 0);
		secondDateTime = new DateTimeValue(1997, 1, 17, 3, 45, 0);

		builder.createRow(new IntValue(11), firstDateTime);
		builder.createRow(new IntValue(10), secondDateTime);
		builder.createRow(new IntValue(9), firstDateTime);
		builder.createRow(new IntValue(5), secondDateTime);

		test1 = builder.build();
		model.add(test1);

	}

	@Test
	public void testParIsDataTable() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(11));
		builder.createRow(new IntValue(5));

		DataTable test2 = builder.build();
		model.add(test2);

		assertFalse(model.getByName("res").isPresent());
		String input = "from(test1, test2)|is(test2, res)";
		CombinedDataTable result = (CombinedDataTable) parseAndProcess(input);
		CombinedDataTable check = (CombinedDataTable) parseAndProcess("from(test1, test2)");
		assertTrue(check.equalsSoft(result));
		assertTrue(model.getByName("res").isPresent());

		assertTrue(test2.equalsSoft(model.getByName("res").get()));
	}

	@Test
	public void testParseDiff() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("date", DateTimeValue.class);

		builder.createRow(new IntValue(11), firstDateTime);
		builder.createRow(new IntValue(5), secondDateTime);

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "difference(test1, test2)";
		Table result = parseAndProcess(input);

		builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("date", DateTimeValue.class);

		builder.createRow(new IntValue(10), secondDateTime);
		builder.createRow(new IntValue(9), firstDateTime);

		DataTable expected = builder.build();
		assertTrue(expected.equalsSoft(result));
	}

	@Test
	public void testParseUnion() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("date", DateTimeValue.class);

		builder.createRow(new IntValue(11), firstDateTime);
		builder.createRow(new IntValue(3), secondDateTime);

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "union(test1, test2)";
		Table result = parseAndProcess(input);

		builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("date", DateTimeValue.class);

		builder.createRow(new IntValue(11), firstDateTime);
		builder.createRow(new IntValue(10), secondDateTime);
		builder.createRow(new IntValue(9), firstDateTime);
		builder.createRow(new IntValue(5), secondDateTime);
		builder.createRow(new IntValue(3), secondDateTime);

		DataTable expected = builder.build();
		assertTrue(expected.equalsSoft(result));
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
		String input = "def isTen : Constraint = test1.value = 10;\n" +
				"from(test1)|constraint(isTen)";


		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);
		DataTable table = (DataTable) result;

		assertEquals(1, table.getRowCount());
		DataRow row = table.getRow(0);
		assertEquals(new IntValue(10), row.getValue(table.getColumn("value")));
	}

	private Table parseAndProcess(String input) throws Exception {
		Parser parser = new Parser();
		DataProcess process = parser.parse(input, model);

		return process.process();
	}

	@Test
	public void testParseFromGreaterConstraint() throws Exception {
		String input = "def gtTen : Constraint = test1.value > 10;\n" +
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
		String input = "def ltTen : Constraint = test1.value < 10;\n" +
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
		String input = "def gtEqTen : Constraint = test1.value >= 10;\n" +
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
		String input = "def gtTen : Constraint = (test2.value > 10);" +
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

		String input = "def isTrue : Constraint = test2.value;" +
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
				"def codeCheck : Constraint = HAS_CODE(\"test\") OR HAS_CODE(test2.code);" +
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
		String input = "def gtNine : Constraint = test1.value > 9;\n" +
				"from(test1)|constraint(gtNine)|setCode(\"hallo\", test1)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertTrue(table.getRow(0).getCodes().contains("hallo"));
		assertTrue(table.getRow(1).getCodes().contains("hallo"));
		assertFalse(table.getRow(2).getCodes().contains("hallo"));
		assertFalse(table.getRow(3).getCodes().contains("hallo"));
	}

	@Test
	public void testParseDateComparison() throws Exception {
		String input =
				"def beforeOneYear : Constraint = test1.date BEFORE #1996-01-17 10:00:33#;\n" +
				"from(test1)|constraint(beforeOneYear)|is(result)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		DataRow row1 = table.getRow(0);
		DataRow row3 = table.getRow(1);
		assertEquals(new IntValue(11), row1.getValue(table.getColumn("value")));


		assertEquals(new IntValue(9), row3.getValue(table.getColumn("value")));
	}

	@Test
	public void testParseTimeComparison() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("time", TimeValue.class);

		builder.createRow(new TimeValue(11, 0, 0));
		builder.createRow(new TimeValue(12, 0, 0));
		builder.createRow(new TimeValue(13, 0, 0));

		DataTable test2 = builder.build();
		model.add(test2);

		String input =
				"def beforeTwelveTwelve : Constraint = test2.time BEFORE #12:12#;\n" +
						"from(test2)|constraint(beforeTwelveTwelve)|is(result)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		DataRow row1 = table.getRow(0);
		DataRow row3 = table.getRow(1);
		assertEquals(new TimeValue(11, 0, 0), row1.getValue(table.getColumn("time")));
		assertEquals(new TimeValue(12, 0, 0), row3.getValue(table.getColumn("time")));
	}

	@Test
	public void testParseCount() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");

		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(1));
		builder.createRow(new IntValue(2));

		model.add(builder.build());

		String input = "def isMax : Constraint = test2.value = COUNT(test2.value);\n" +
				"from(test2)|constraint(isMax)|is(result)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		DataRow row1 = table.getRow(0);
		assertEquals(1, table.getRowCount());

		assertEquals(2, row1.getValue(table.getColumn("value")).getValue());
	}

	@Test
	public void testParseGroupByColumn() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("value2", IntValue.class);

		builder.createRow(new IntValue(11), new IntValue(10));
		builder.createRow(new IntValue(11), new IntValue(5));
		builder.createRow(new IntValue(5), new IntValue(3));

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "def group : GroupByColumn = " +
				"NAME sjon ON (test2.value * 2) " +
				"FROM MAX(test2.value2) AS max, AVERAGE(test2.value2) AS avg;\n" +
				"from(test2)|groupBy(group)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertEquals(new StringValue("22"), table.getRow(0).getValue(table.getColumn("Chunk")));
		assertEquals(new IntValue(10), table.getRow(0).getValue(table.getColumn("max")));
		assertEquals(new FloatValue(7.5f), table.getRow(0).getValue(table.getColumn("avg")));

		assertEquals(new IntValue(3), table.getRow(1).getValue(table.getColumn("max")));
		assertEquals(new FloatValue(3f), table.getRow(1).getValue(table.getColumn("avg")));
	}

	@Test
	public void testParseGroupByConstraints() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);
		builder.createColumn("value2", IntValue.class);

		builder.createRow(new IntValue(11), new IntValue(10));
		builder.createRow(new IntValue(11), new IntValue(5));
		builder.createRow(new IntValue(5), new IntValue(3));

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "def group : GroupByConstraint = " +
				"NAME sjon ON " +
				"test2.value < 10 AS first, " +
				"test2.value > 10 AS second " +
				"FROM MAX(test2.value2) AS max, AVERAGE(test2.value2) AS avg;\n" +
				"from(test2)|groupBy(group)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertEquals(new StringValue("first"), table.getRow(0).getValue(table.getColumn("Chunk")));
		assertEquals(new IntValue(3), table.getRow(0).getValue(table.getColumn("max")));
		assertEquals(new FloatValue(3f), table.getRow(0).getValue(table.getColumn("avg")));

		assertEquals(new StringValue("second"), table.getRow(1).getValue(table.getColumn("Chunk")));
		assertEquals(new IntValue(10), table.getRow(1).getValue(table.getColumn("max")));
		assertEquals(new FloatValue(7.5f), table.getRow(1).getValue(table.getColumn("avg")));
	}

	@Test( expected = ParseException.class )
	public void testIncorrectMacroType() throws Exception {
		String input = "def incorrectType : TypeDoesntExists = anything;";

		parseAndProcess(input);
	}

	@Test( expected = ParseException.class )
	public void testIncorrectProcessChain() throws Exception {
		parseAndProcess("from(test)|");
	}

	@Test( expected = ParseException.class )
	public void testIncorrectConstraints() throws Exception {
		String input = "def stuff : Constraint = 5 <;test()";
		parseAndProcess(input);
	}

	@Test
	public void testParseSort() throws Exception {
		String input = "from(test1)|sort(test1.value, \"ASC\")";
		Table result = parseAndProcess(input);

		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertEquals(new IntValue(5), table.getRow(0).getValue(table.getColumn("value")));
		assertEquals(new IntValue(9), table.getRow(1).getValue(table.getColumn("value")));
		assertEquals(new IntValue(10), table.getRow(2).getValue(table.getColumn("value")));
		assertEquals(new IntValue(11), table.getRow(3).getValue(table.getColumn("value")));
	}

	@Test
	public void testJoin() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(null));
		builder.createRow(new IntValue(null));
		builder.createRow(new IntValue(null));
		builder.createRow(new IntValue(null));

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "def join : Join = JOIN test1 WITH test2 AS joined" +
				" FROM test1.value AND test2.value;" +
				"join(join)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertEquals(new IntValue(11), table.getRow(0).getValue(table.getColumn("value")));
		assertEquals(new IntValue(11), table.getRow(1).getValue(table.getColumn("value")));
		assertEquals(new IntValue(11), table.getRow(2).getValue(table.getColumn("value")));
		assertEquals(new IntValue(11), table.getRow(3).getValue(table.getColumn("value")));
		assertEquals(new IntValue(10), table.getRow(4).getValue(table.getColumn("value")));
		assertEquals(new IntValue(9), table.getRow(8).getValue(table.getColumn("value")));
		assertEquals(new IntValue(5), table.getRow(12).getValue(table.getColumn("value")));
	}

	@Test
	public void testJoinWithConstraint() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(11));
		builder.createRow(new IntValue(10));
		builder.createRow(new IntValue(5));
		builder.createRow(new IntValue(3));

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "def join : Join = JOIN test1 WITH test2 AS joined" +
				" ON  test1.value = test2.value" +
				" FROM test1.value AND test2.value;" +
				"join(join)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertEquals(new IntValue(11), table.getRow(0).getValue(table.getColumn("value")));
		assertEquals(new IntValue(10), table.getRow(1).getValue(table.getColumn("value")));
		assertEquals(new IntValue(5), table.getRow(2).getValue(table.getColumn("value")));
	}

	@Test
	public void testConnection() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("value", IntValue.class);

		builder.createRow(new IntValue(11));
		builder.createRow(new IntValue(10));
		builder.createRow(new IntValue(5));
		builder.createRow(new IntValue(3));

		DataTable test2 = builder.build();
		model.add(test2);

		String input = "def con : Connection = test1 WITH test2 AS joined" +
				" FROM test1.value AND test2.value;" +
				"connection(con)";

		Table result = parseAndProcess(input);
		assertTrue(result instanceof DataTable);

		DataTable table = (DataTable) result;

		assertEquals(new IntValue(3), table.getRow(0).getValue(table.getColumn("value")));
		assertEquals(new IntValue(5), table.getRow(1).getValue(table.getColumn("value")));
		assertEquals(new IntValue(9), table.getRow(3).getValue(table.getColumn("value")));
		assertEquals(new IntValue(10), table.getRow(4).getValue(table.getColumn("value")));
	}
}