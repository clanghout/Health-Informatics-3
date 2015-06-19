package model.language;

import model.data.DataModel;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.exceptions.ParseException;
import model.language.nodes.FunctionNode;
import model.language.nodes.ValueNode;
import model.process.DataProcess;
import model.process.analysis.ConstraintAnalysis;
import model.process.analysis.GroupByColumn;
import model.process.analysis.GroupByConstraint;
import model.process.analysis.operations.ColumnComputation;
import model.process.analysis.LagSequentialAnalysis;
import model.process.analysis.operations.Connection;
import model.process.functions.Function;
import model.process.setOperations.FullJoin;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
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

	DataProcess parse(String body, DataModel model) throws ParseException {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);

		switch (type) {
			case "Constraint":
				return parseConstraint(body, model, parser);
			case "GroupByColumn":
				return parseGroupByColumn(body, model, parser);
			case "GroupByConstraint":
				return parseGroupByConstraints(body, model, parser);
			case "Join":
				return parseJoin(body, model, parser);
			case "Connection":
				return parseConnection(body, model, parser);
			case "Computation":
				return parseComputation(body, model, parser);
			case "Comparison":
				return parseComparison(body, model, parser);
			default:
				throw new ParseException(String.format("Macro type %s isn't supported", type));
		}
	}

	private DataProcess parseComputation(String body, DataModel model, LanguageParser parser)
			throws ParseException {
		ReportingParseRunner runner = new ReportingParseRunner(parser.ColumnComputation());
		ParsingResult result = runner.run(body);

		if (result.matched) {
			Identifier<DataTable> name = (Identifier<DataTable>) result.valueStack.pop();
			String columnCompType = (String) result.valueStack.pop();
			List<ValueNode<DataValue>> values = (List<ValueNode<DataValue>>)
					result.valueStack.pop();
			List<Identifier> identifiers = (List<Identifier>) result.valueStack.pop();

			ColumnComputation comp = new ColumnComputation(
					name.getName(),
					columnCompType.equals("INCLUDE EXISTING")
			);

			for (int i = 0; i < identifiers.size(); i++) {
				comp.addColumn(values.get(i).resolve(model), identifiers.get(i).getName());
			}

			return comp;
		} else {
			throw new ParseException("Couldn't parse Computation", result.parseErrors);
		}
	}

	private DataProcess parseComparison(String body, DataModel model, LanguageParser parser)
			throws ParseException {
		ReportingParseRunner runner = new ReportingParseRunner(parser.LagSequential());
		ParsingResult result = runner.run(body);

		if (result.matched) {
			Identifier<DataTable> leftTable = (Identifier<DataTable>) result.valueStack.pop();
			Identifier<DataTable> rightTable = (Identifier<DataTable>) result.valueStack.pop();
			Identifier<DataTable> resultName = (Identifier<DataTable>) result.valueStack.pop();
			ColumnIdentifier firstColumn = (ColumnIdentifier) result.valueStack.pop();
			ColumnIdentifier secondColumn = (ColumnIdentifier) result.valueStack.pop();

			LagSequentialAnalysis analysis = new LagSequentialAnalysis(
					leftTable,
					new Identifier<>(firstColumn.getColumn()),
					rightTable,
					new Identifier<>(secondColumn.getColumn())
			);

			analysis.setName(resultName.getName());

			return analysis;
		} else {
			throw new ParseException("Failed to parse Comparison", result.parseErrors);
		}
	}

	private DataProcess parseConnection(String body, DataModel model, LanguageParser parser)
			throws ParseException {
		ReportingParseRunner runner = new ReportingParseRunner(parser.Connection());
		ParsingResult result = runner.run(body);

		if (result.matched) {
			ColumnIdentifier leftColumn = (ColumnIdentifier) result.valueStack.pop();
			Identifier<DataTable> rightTable = (Identifier<DataTable>) result.valueStack.pop();
			ColumnIdentifier rightColumn = (ColumnIdentifier) result.valueStack.pop();
			Identifier name = (Identifier) result.valueStack.pop();
			List<ColumnIdentifier> leftColumns = (List<ColumnIdentifier>) result.valueStack.pop();
			List<ColumnIdentifier> rightColumns = (List<ColumnIdentifier>) result.valueStack.pop();
			Identifier<DataTable> leftTable = (Identifier<DataTable>) result.valueStack.pop();


			Connection connection = new Connection(
					name.getName(),
					leftTable,
					leftColumn,
					rightTable,
					rightColumn);

			for (int i = 0; i < leftColumns.size(); i++) {
				connection.addOverlappingColumns(leftColumns.get(i), rightColumns.get(i));
			}

			return connection;
		} else {
			throw new ParseException("Failed to parse Connection", result.parseErrors);
		}
	}

	private DataProcess parseJoin(String body, DataModel model, LanguageParser parser)
			throws ParseException {
		ReportingParseRunner runner = new ReportingParseRunner(parser.Join());
		ParsingResult result = runner.run(body);

		if (result.matched) {
			Identifier<DataTable> leftTable = (Identifier<DataTable>) result.valueStack.pop();
			Identifier<DataTable> rightTable = (Identifier<DataTable>) result.valueStack.pop();
			Identifier name = (Identifier) result.valueStack.pop();
			ValueNode<BoolValue> constraint = (ValueNode<BoolValue>) result.valueStack.pop();
			List<ColumnIdentifier> leftColumns = (List<ColumnIdentifier>) result.valueStack.pop();
			List<ColumnIdentifier> rightColumns = (List<ColumnIdentifier>) result.valueStack.pop();
			String joinType = (String) result.valueStack.pop();

			FullJoin.Join type = resolveJoinType(joinType);
			FullJoin join = new FullJoin(
					name.getName(),
					leftTable,
					rightTable,
					type
			);

			for (int i = 0; i < rightColumns.size(); i++) {
				join.addCombineColumn(
						leftColumns.get(i),
						rightColumns.get(i)
				);
			}

			if (constraint != null) {
				join.setConstraint(constraint.resolve(model));
			}

			return join;
		} else {
			throw new ParseException("Failed to parse Join", result.parseErrors);
		}
	}

	private FullJoin.Join resolveJoinType(String type) throws ParseException {
		switch (type) {
			case "FULL JOIN": return FullJoin.Join.FULL;
			case "LEFT JOIN": return FullJoin.Join.LEFT;
			case "RIGHT JOIN": return FullJoin.Join.RIGHT;
			case "JOIN": return FullJoin.Join.JOIN;
			default:
				throw new ParseException(String.format("Type %s is not a valid join type", type));
		}
	}

	private DataProcess parseConstraint(String body, DataModel model, LanguageParser parser)
			throws ParseException {
		ReportingParseRunner runner = new ReportingParseRunner(parser.BooleanExpression());
		ParsingResult result = runner.run(body);
		if (result.matched) {
			ValueNode<BoolValue> node = (ValueNode<BoolValue>) result.valueStack.pop();
			ConstraintAnalysis constraintAnalysis = new ConstraintAnalysis(node.resolve(model));
			constraintAnalysis.setDataModel(model);
			return constraintAnalysis;
		} else {
			throw new ParseException("Failed to parse Constraint", result.parseErrors);
		}
	}

	private DataProcess parseGroupByColumn(String body, DataModel model, LanguageParser parser)
			throws ParseException {
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
			throw new ParseException("Failed to parse GroupByColumn", result.parseErrors);
		}
	}

	private DataProcess parseGroupByConstraints(
			String body,
			DataModel model,
			LanguageParser parser) throws ParseException {
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
			throw new ParseException("Failed to parse GroupByConstraints", result.parseErrors);
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
		ReportingParseRunner runner = new ReportingParseRunner(parser.GroupByColumn());
		return runner.run(body);
	}

	private ParsingResult tryParseConstraints(String body, LanguageParser parser) {
		ReportingParseRunner runner = new ReportingParseRunner(parser.GroupByConstraints());
		return runner.run(body);
	}
}
