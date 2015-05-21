package language;

import model.data.DataModel;
import model.data.process.analysis.operations.Operation;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;

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

	Operation parse(String body, DataModel model) {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);
		if (type.equals("Constraint")) {
			BasicParseRunner runner = new BasicParseRunner(parser.Comparison());
			CompareNode node = (CompareNode) runner.getValueStack().pop();
			return node.resolve(model);
		} else {
			throw new UnsupportedOperationException("Code has not yet been implemented");
		}
	}
}
