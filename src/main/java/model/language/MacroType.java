package model.language;

import model.data.DataModel;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.language.nodes.FunctionNode;
import model.language.nodes.ValueNode;
import model.process.analysis.ConstraintAnalysis;
import model.process.analysis.GroupByColumn;
import model.process.analysis.GroupByConstraint;
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

		switch (type) {
			case "Constraint":
				return parseConstraint(body, model, parser);
			case "GroupByColumn":
				return parseGroupByColumn(body, model, parser);
			case "GroupByConstraint":
				return parseGroupByConstraints(body, model, parser);
			default:
				throw new UnsupportedOperationException("Code has not yet been implemented");
		}
	}

	private Object parseConstraint(String body, DataModel model, LanguageParser parser) {
		BasicParseRunner runner = new BasicParseRunner(parser.BooleanExpression());
		ParsingResult result = runner.run(body);
		ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
		return node.resolve(model);
	}

	private Object parseGroupByColumn(String body, DataModel model, LanguageParser parser) {
		ParsingResult result;

		if ((result = tryParseColumns(body, parser)).matched) {
			Identifier<DataTable> name = (Identifier<DataTable>) result.valueStack.pop();
			DataDescriber<?> column = ((ValueNode<?>) result.valueStack.pop()).resolve(model);
			List<Function> functions = resolveGroupByFunctions(model, result);
			List<String> columnNames = resolveGroupByColumnNames(result);

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

	private Object parseGroupByConstraints(String body, DataModel model, LanguageParser parser) {
		ParsingResult result;
		if ((result = tryParseConstraints(body, parser)).matched) {
			Identifier<DataTable> name = (Identifier<DataTable>) result.valueStack.pop();
			List<ValueNode<BoolValue>> constraints =
					(List<ValueNode<BoolValue>>) result.valueStack.pop();
			List<Identifier> groupNames = (List<Identifier>) result.valueStack.pop();
			List<Function> functions = resolveGroupByFunctions(model, result);
			List<String> columnNames = resolveGroupByColumnNames(result);

			List<ConstraintAnalysis> cons = constraints
					.stream()
					.map((x) -> {
						ConstraintAnalysis analysis = new ConstraintAnalysis(x.resolve(model));
						analysis.setDataModel(model);
						return analysis;
					}).collect(Collectors.toList());


			return new GroupByConstraint(
					name.getName(),
					cons,
					identifiersToStrings(groupNames),
					functions,
					columnNames
			);
		} else {
			return null;
		}
	}

	private List<String> resolveGroupByColumnNames(ParsingResult result) {
		List<Identifier> columnNameIdentifiers = (List<Identifier>) result.valueStack.pop();
		return identifiersToStrings(columnNameIdentifiers);
	}

	private List<String> identifiersToStrings(List<Identifier> columnNameIdentifiers) {
		return columnNameIdentifiers
				.stream()
				.map(Identifier::getName)
				.collect(Collectors.toList());
	}

	private List<Function> resolveGroupByFunctions(DataModel model, ParsingResult result) {
		List<FunctionNode> functionNodes = (List<FunctionNode>) result.valueStack.pop();

		return functionNodes.stream()
				.map((node) -> node.getFunction(model))
				.collect(Collectors.toList());
	}

	private ParsingResult tryParseColumns(String body, LanguageParser parser) {
		BasicParseRunner runner = new BasicParseRunner(parser.GroupByColumn());
		return runner.run(body);
	}

	private ParsingResult tryParseConstraints(String body, LanguageParser parser) {
		BasicParseRunner runner = new BasicParseRunner(parser.GroupByConstraints());
		return runner.run(body);
	}
}
