package model.language;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.BasicParseRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * This class contains the parse match tests for the LanguageParser.
 *
 * Created by Boudewijn on 20-5-2015.
 */
@RunWith(Parameterized.class)
public class LanguageParserMatchTest {

	private Rule rule;
	private String input;
	private boolean match;

	public LanguageParserMatchTest(Rule rule, String input, boolean match) {
		this.rule = rule;
		this.input = input;
		this.match = match;
	}


	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);

		Object[][] testData = new Object[][] {
				{ parser.Identifier(), "abOis9d", true },
				{ parser.Identifier(), "92", false },
				{ parser.StringLiteral(), "\"dinge niu!h\"", true },
				{ parser.StringLiteral(), "\"\"", true },
				{ parser.StringLiteral(), "\"dsa", false },
				{ parser.Digit(), "9", true },
				{ parser.Digit(), "abS", false },
				{ parser.FloatLiteral(), "312.412", true },
				{ parser.FloatLiteral(), "2", false },
				{ parser.FloatLiteral(), "s", false },
				{ parser.IntLiteral(), "4", true }, // 10
				{ parser.IntLiteral(), "415", true },
				{ parser.Params(), "()", true },
				{ parser.Params(), "(test)", true },
				{ parser.Params(), "(test,dingen)", true },
				{ parser.Params(), "(test, dingen, nogMeer)", true },
				{ parser.Params(), "(test, 5.0, 3, \"Test\")", true },
				{ parser.Process(), "test()", true },
				{ parser.Pipe(), "test()|test2()", true },
				{ parser.ColumnIdentifier(), "test.dignen", true },
				{ parser.ColumnIdentifier(), "test", false }, // 20
				{ parser.Macro(), "def test : Constraint = test;", true},
				{ parser.CompareOperator(), ">=", true},
				{ parser.Comparison(), "test.dingen >= 10", true},
				{ parser.BooleanExpression(), "true", true},
				{ parser.BooleanExpression(), "false", true},
				{ parser.BooleanExpression(), "5 = 5", true},
				{ parser.BooleanExpression(), "5 = 5 AND true", true},
				{ parser.BooleanExpression(), "NOT(false)", true},
				{ parser.NumberExpression(), "5 * 5", true},
				{ parser.NumberExpression(), "(5 * 5) + 3", true}, // 30
				{ parser.BooleanExpression(), "HAS_CODE(\"test\")", true},
				{ parser.DateTimeLiteral(), "#1995-01-17 03:45#", true},
				{ parser.DateTimeLiteral(), "#1995-01-17 03:35:33#", true},
				{ parser.DateLiteral(), "#1995-01-17#", true},
				{ parser.NumberExpression(), "MAX(table.column)", true},
				{ parser.GroupByColumn(), "NAME sjon ON table.column", true},
				{ parser.GroupByColumn(),
						"NAME sjon ON table.column FROM MAX(table.column) AS max", true},
				{
						parser.GroupByConstraints(),
						"NAME sjon ON table.column < 5 AS test FROM MAX(table.column) AS max",
						true
				},
				{ parser.Sugar(), "test()", true},
				{ parser.Sugar(), "def test : Macro = dingen; \ntest()", true}, // 40
				{ parser.Sugar(), "test(argument)|from()", true},
				{ parser.JoinColumn(), "table.column AND thing.column", true},
				{ parser.Join(), "JOIN table1 WITH table2 AS test", true},
				{
						parser.Join(),
						"JOIN table1 WITH table2 AS test FROM table1.col AND test.sjoin",
						true
				},
				{
						parser.Join(),
						"JOIN table1 WITH table2 AS test ON table1.test = table2.stuff " +
								"FROM table1.col AND test.sjoin",
						true
				},
				{
						parser.Connection(),
						"table1 ON table1.value WITH table2 ON table2.value AS test",
						true
				},
				{
						parser.Connection(),
						"table1 ON table1.value WITH table2 ON table2.value AS test " +
								"FROM table1.col AND table2.col",
						true
				},
				{ parser.Sugar(), "test() |\n shit()    \t| stuff()", true},
				{ parser.Sugar(), "def test : Test = a; def test2 : Stuff = b; test()", true}
		};

		return Arrays.asList(testData);
	}

	@Test
	public void test() throws Exception {
		assertEquals(match, new BasicParseRunner<>(rule).run(input).matched);
	}
}