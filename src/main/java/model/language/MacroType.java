package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

/**
 * Represents the type of a Macro.
 *
 * Created by Boudewijn on 21-5-2015.
 */
class MacroType {

	private final String type;

	MacroType(String type) {
		this.type = type;
	}

	DataDescriber parse(String body, DataModel model) {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);
		if (type.equals("Constraint")) {
			BasicParseRunner runner = new BasicParseRunner(parser.BooleanOperation());
			ParsingResult result = runner.run(body);
			BooleanNode node = (BooleanNode) result.valueStack.pop();
			return node.resolve(model);
		} else {
			throw new UnsupportedOperationException("Code has not yet been implemented");
		}
	}
}
