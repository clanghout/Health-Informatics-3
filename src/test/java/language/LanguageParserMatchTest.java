package language;

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
				{ parser.IntLiteral(), "4", true },
				{ parser.IntLiteral(), "415", true },
				{ parser.Params(), "()", true },
				{ parser.Params(), "(test)", true },
				{ parser.Params(), "(test,dingen)", true },
				{ parser.Params(), "(test, dingen, nogMeer)", true },
				{ parser.Params(), "(test, 5.0, 3, \"Test\")", true },
				{ parser.Process(), "test()", true },
				{ parser.Pipe(), "test()|test2()", true },
				{ parser.ColumnIdentifier(), "test.dignen", true },
				{ parser.ColumnIdentifier(), "test", false },
				{ parser.Macro(), "def test() : Constraint = test;", true},
				{ parser.CompareOperator(), "=", true},
				{ parser.Comparison(), "test.dingen = 10", true}
		};

		return Arrays.asList(testData);
	}

	@Test
	public void test() throws Exception {
		assertEquals(match, new BasicParseRunner<>(rule).run(input).matched);
	}
}