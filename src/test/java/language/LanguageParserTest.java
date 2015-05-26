package language;

import org.junit.Before;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		assertArrayEquals(new Object[]{3, 4, 5}, info.getParameters());
	}

	@Test
	public void testPipe() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.Pipe());
		ParsingResult result = runner.run("test()|test2(\"aap\", sjon, 2.0)");
		ProcessInfo info = (ProcessInfo) result.valueStack.pop();

		assertEquals("test2", info.getIdentifier().getName());
		assertArrayEquals(
				new Object[]{"aap", new Identifier<>("sjon"), 2.0f},
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
		ParsingResult result = runner.run("def test() : Constraint = test1.value = 10;");

		MacroInfo info = (MacroInfo) result.valueStack.pop();

		assertEquals("test", info.getIdentifier().getName());
	}

	@Test
	public void testBooleanLiteral() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		ParsingResult result = runner.run("true");

		assertTrue(result.matched);
		BooleanNode node = (BooleanNode) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testBooleanOperation() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanOperation());
		ParsingResult result = runner.run("false OR true");

		assertTrue(result.matched);
		BooleanNode node = (BooleanNode) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testBooleanExpression() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanOperation());
		String input = "((false AND true) OR (5 = 5 AND (5 > 2 OR (2 > 5 OR NOT(false)))))" +
				" AND NOT(5 < 2)";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		BooleanNode node = (BooleanNode) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}

	@Test
	public void testNotOperation() throws Exception {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		String input = "NOT(false)";
		ParsingResult result = runner.run(input);

		assertTrue(result.matched);
		BooleanNode node = (BooleanNode) result.valueStack.pop();
		assertTrue(node.resolve(null).resolve(null).getValue());
	}
}