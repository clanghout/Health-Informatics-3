package model.language;

import model.data.DataModel;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.language.nodes.FunctionNode;
import model.language.nodes.ValueNode;
import model.process.analysis.GroupByColumn;
import model.process.functions.Function;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import java.util.List;
import java.util.stream.Collectors;

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

	Object parse(String body, DataModel model) {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);
		if (type.equals("Constraint")) {
			return parseConstraint(body, model, parser);
		} else if (type.equals("GroupBy")) {
			return parseGroupBy(body, model, parser);
		} else {
			throw new UnsupportedOperationException("Code has not yet been implemented");
		}
	}

	private Object parseConstraint(String body, DataModel model, LanguageParser parser) {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		ParsingResult result = runner.run(body);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		return node.resolve(model);
	}

	private Object parseGroupBy(String body, DataModel model, LanguageParser parser) {
		BasicParseRunner runner = new BasicParseRunner(parser.GroupByColumn());
		ParsingResult result = runner.run(body);

		if (result.matched) {
			Identifier<DataTable> name = (Identifier<DataTable>) result.valueStack.pop();
			DataDescriber<?> column = ((ValueNode<?>) result.valueStack.pop()).resolve(model);
			List<FunctionNode> functionNodes = (List<FunctionNode>) result.valueStack.pop();
			List<Identifier> columnNameIdentifiers = (List<Identifier>) result.valueStack.pop();

			List<Function> functions = functionNodes.stream()
					.map((node) -> node.getFunction(model))
					.collect(Collectors.toList());

			List<String> columnNames = columnNameIdentifiers
					.stream()
					.map(Identifier::getName)
					.collect(Collectors.toList());

			return new GroupByColumn(
					name.getName(),
					column,
					functions,
					columnNames
			);
		} else {
			return null;
		}
	}
}
