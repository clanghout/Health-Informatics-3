package model.language;

import model.data.value.*;
import model.language.nodes.ConstantNode;
import model.language.nodes.FunctionNode;
import model.language.nodes.ValueNode;
import org.junit.Before;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Contains the more complicated tests for LanguageParser.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class LanguageParserTest {

	private LanguageParser parser;

	@Before
	public void setUp() throws Exception {
		parser = Parboiled.createParser(LanguageParser.class);
	}

	@Test
	public void testProcess() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.Process());
		ParsingResult result = runner.run("test(3, 4, 5)");
		ProcessInfo info = (ProcessInfo) result.valueStack.pop();

		assertEquals("test", info.getIdentifier().getName());
		Object[] expected = Arrays.stream(new Integer[]{3, 4, 5})
				.map(x -> new ConstantNode<IntValue>(new IntValue(x)))
				.toArray(Object[]::new);
		assertArrayEquals(expected, info.getParameters());
	}

	@Test
	public void testPipe() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.Pipe());
		ParsingResult result = runner.run("test()|test2(\"aap\", sjon, 2.0)");
		ProcessInfo info = (ProcessInfo) result.valueStack.pop();

		assertEquals("test2", info.getIdentifier().getName());
		assertArrayEquals(
				new Object[]{
						new ConstantNode<>(new StringValue("aap")),
						new Identifier<>("sjon"),
						new ConstantNode<>(new FloatValue(2.0f))
				},
				info.getParameters()
		);

		info = (ProcessInfo) result.valueStack.pop();

		assertEquals("test", info.getIdentifier().getName());
		assertArrayEquals(
				new Object[]{},
				info.getParameters()
		);
	}

	@Test
	public void testMacro() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.Macro());
		ParsingResult result = runner.run("def test : Constraint = test1.value = 10;");

		MacroInfo info = (MacroInfo) result.valueStack.pop();

		assertEquals("test", info.getIdentifier().getName());
	}

	@Test
	public void testBooleanLiteral() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		ParsingResult result = runner.run("true");

		assertTrue(result.matched);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testBooleanOperation() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanOperation());
		ParsingResult result = runner.run("false OR true");

		assertTrue(result.matched);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testBooleanExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanOperation());
		String input = "((false AND true) OR (5 = 5 AND (5 > 2 OR (2 > 5 OR NOT(false)))))" +
				" AND NOT(5 < 2 + 1)";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testNotOperation() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		String input = "NOT(false)";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testNumberExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.NumberExpression());
		String input = "SQRT((((5 * 5) + (3 - 4)) / 2) ^2)";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<NumberValue> node = (ValueNode<NumberValue>) result.valueStack.pop();
		assertEquals(12.0f, node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testParenthesizedBooleanExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		String input = "((true))";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testParenthesizedNumberExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.NumberExpression());
		String input = "((5.0))";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<NumberValue> node = (ValueNode<NumberValue>) result.valueStack.pop();
		assertEquals(5.0f, node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testPeriodLiteral() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.PeriodLiteral());
		String input = "#5 DAYS#";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<PeriodValue> node = (ValueNode<PeriodValue>) result.valueStack.pop();
		assertEquals(Period.of(0, 0, 5), node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testDateTimeExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.DateExpression());
		String input = "#1995-01-17 03:43#";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<DateTimeValue> node = (ValueNode<DateTimeValue>) result.valueStack.pop();
		assertEquals(
				LocalDateTime.of(1995, 1, 17, 3, 43, 0),
				node.resolve(null).resolve(null).getValue()
		);
	}

	@Test
	public void testDateExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.DateExpression());
		String input = "#1995-01-17#";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<DateValue> node = (ValueNode<DateValue>) result.valueStack.pop();
		assertEquals(
				LocalDate.of(1995, 1, 17),
				node.resolve(null).resolve(null).getValue()
		);
	}

	@Test
	public void testDateComparison() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		String input = "#1995-01-17 10:00:23# BEFORE #1996-01-17 10:00:30#";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testDateOperation() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.DateExpression());
		String input = "(#1994-01-16# ADD #1 YEARS#) ADD #1 DAYS#";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<DateValue> node = (ValueNode<DateValue>) result.valueStack.pop();
		assertEquals(LocalDate.of(1995, 1, 17), node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testRelative() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.NumberExpression());
		String input = "RELATIVE(#1995-01-17#, #2015-06-09#, DAYS)";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		ValueNode<IntValue> node = (ValueNode<IntValue>) result.valueStack.pop();
		assertEquals(7448, (int) node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testGroupByFunctions1() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.GroupByFunctions());
		String input = "";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		List<Identifier> identifiers = (List<Identifier>) result.valueStack.pop();
		List<FunctionNode> functions = (List<FunctionNode>) result.valueStack.pop();

		assertTrue(identifiers.isEmpty());
		assertTrue(functions.isEmpty());
	}

	@Test
	public void testGroupByFunctions2() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.GroupByFunctions());
		String input = " FROM MAX(test1.value) AS sjon";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);

		List<Identifier> identifiers = (List<Identifier>) result.valueStack.pop();
		List<FunctionNode> functions = (List<FunctionNode>) result.valueStack.pop();

		assertEquals(1, identifiers.size());
		assertEquals("sjon", identifiers.get(0).getName());

		assertEquals(1, functions.size());
	}


	@Test
	public void testGroupByFunctions3() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.GroupByFunctions());
		String input = " FROM MAX(test1.value) AS sjon, AVERAGE(test1.value) AS sjaak";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);

		List<Identifier> identifiers = (List<Identifier>) result.valueStack.pop();
		List<FunctionNode> functions = (List<FunctionNode>) result.valueStack.pop();

		assertEquals(2, identifiers.size());
		assertEquals("sjon", identifiers.get(0).getName());
		assertEquals("sjaak", identifiers.get(1).getName());

		assertEquals(2, functions.size());
	}

	@Test
	public void testCombine() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.DateExpression());
		String input = "COMBINE(#1995-01-17#, #12:12#)";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);

		ValueNode<DateTimeValue> node = (ValueNode<DateTimeValue>) result.resultValue;

		assertEquals(new DateTimeValue(1995, 1, 17, 12, 12, 00), node.resolve(null).resolve(null));
	}

	@Test
	public void testExtractTime() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.TimeExpression());
		String input = "TO_TIME(#1995-01-17 12:12#)";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);

		ValueNode<TimeValue> node = (ValueNode<TimeValue>) result.resultValue;

		assertEquals(new TimeValue(12, 12, 00), node.resolve(null).resolve(null));
	}

	@Test
	public void testExtractDate() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.DateExpression());
		String input = "TO_DATE(#1995-01-17 12:12#)";

		ParsingResult result = runner.run(input);

		assertTrue(result.matched);

		ValueNode<DateValue> node = (ValueNode<DateValue>) result.resultValue;

		assertEquals(new DateValue(1995, 1, 17), node.resolve(null).resolve(null));
	}
}