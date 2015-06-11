package model.language;

import model.data.value.*;
import model.language.nodes.*;
import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;
import org.parboiled.support.Var;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

/**
 * The parser for the analysis language.
 * <p/>
 * Since this class is rather declarative in its working, the style deviates from the default
 * Java style and is more PEG based.
 * <p/>
 * Created by Boudewijn on 20-5-2015.
 */
@SuppressWarnings("ALL")
class LanguageParser extends BaseParser<Object> {

	/**
	 * Matches a computation and pushes a NumberOperationNode on the stack.
	 */
	Rule Computation() {
		return Sequence(
				FirstOf(
						FloatLiteral(),
						IntLiteral(),
						NumberColumn(),
						Sequence("(", NumberExpression(), ")")),
				WhiteSpace(),
				NumberOperator(),
				WhiteSpace(),
				FirstOf(
						FloatLiteral(),
						IntLiteral(),
						NumberColumn(),
						Sequence("(", NumberExpression(), ")")),
				swap3(),
				push(
						new NumberOperationNode(
								(ValueNode<NumberValue>) pop(),
								(String) pop(),
								(ValueNode<NumberValue>) pop()
						)
				)
		);
	}

	Rule Sqrt() {
		return Sequence(
				"SQRT(",
				NumberExpression(),
				")",
				push(new NumberOperationNode((ValueNode<NumberValue>) pop(), "SQRT", null))
		);
	}

	/**
	 * Matches any expression resulting in a number.
	 */
	Rule NumberExpression() {
		return FirstOf(
				Sqrt(),
				Computation(),
				FloatLiteral(),
				IntLiteral(),
				NumberColumn(),
				DateFunction(),
				TableFunction(),
				Sequence("(", NumberExpression(), ")")
		);
	}

	/**
	 * Matches a column and pushes a TableValueNode<NumberValue> on the stack.
	 */
	Rule NumberColumn() {
		return Sequence(
				ColumnIdentifier(),
				push(new TableValueNode<NumberValue>((ColumnIdentifier) pop()))
		);
	}

	Rule NumberOperator() {
		return Sequence(
				FirstOf(
						"*", "/", "+", "-", "^"
				),
				push(match())
		);
	}

	Rule TableFunction() {
		return Sequence(
				TableFunctionName(),
				"(",
				WhiteSpace(),
				ColumnIdentifier(),
				WhiteSpace(),
				")",
				swap(),
				push(
						new FunctionNode(
								(String) pop(),
								(ColumnIdentifier) pop()
						)
				)
		);
	}

	Rule TableFunctionName() {
		return Sequence(
				FirstOf(
						"COUNT",
						"AVERAGE",
						"MIN",
						"MAX",
						"SUM",
						"MEDIAN",
						"STDDEV"
				),
				push(match())
		);
	}

	Rule Identifier() {
		return Sequence(
				Sequence(
						SafeCharacter(),
						OneOrMore(
								FirstOf(
										Digit(),
										SafeCharacter()
								)
						)
				),
				push(new Identifier<>(match()))
		);
	}

	Rule StringLiteral() {
		return Sequence(
				"\"",
				ZeroOrMore(Character()),
				push(matchOrDefault("")),
				"\"",
				push(new ConstantNode<StringValue>(new StringValue((String) pop())))
		);
	}

	Rule Character() {
		return NormalCharacter();
	}

	Rule NormalCharacter() {
		return Sequence(TestNot(AnyOf("\"")), ANY);
	}

	Rule SafeCharacter() {
		return FirstOf(
				CharRange('a', 'z'),
				CharRange('A', 'Z')
		);
	}

	Rule IntLiteral() {
		return Sequence(
				OneOrMore(Digit()),
				push(
						new ConstantNode<IntValue>(
								new IntValue(Integer.parseInt(matchOrDefault("0")))
						)
				)
		);
	}

	Rule FloatLiteral() {
		return Sequence(
				Sequence(
						OneOrMore(Digit()),
						".",
						OneOrMore(Digit())
				),
				push(new ConstantNode<FloatValue>(
								new FloatValue(Float.parseFloat(matchOrDefault("0.0")))
						)
				)
		);
	}

	Rule PeriodLiteral() {
		return Sequence(
				"#",
				IntLiteral(),
				WhiteSpace(),
				PeriodUnit(),
				"#",
				swap(),
				push(new PeriodNode((ValueNode<IntValue>) pop(), (String) pop()))
		);
	}

	Rule PeriodUnit() {
		return Sequence(
				FirstOf(
						"DAYS",
						"MONTHS",
						"YEARS"
				),
				push(match())
		);
	}

	Rule ChronoUnit() {
		return FirstOf(
				PeriodUnit(),
				Sequence(
						"HOURS",
						"MINUTES",
						"SECONDS"
				),
				push(match())
		);
	}

	Rule DateExpression() {
		return FirstOf(
				DateCalculation(),
				Sequence("(", DateExpression(), ")"),
				DateTimeLiteral(),
				DateLiteral(),
				DateColumn()
		);
	}

	Rule DateTerm() {
		return FirstOf(
				DateTimeLiteral(),
				DateLiteral(),
				DateColumn(),
				Sequence("(", DateExpression(), ")")
		);
	}

	Rule DateCalculation() {
		return Sequence(
				DateTerm(),
				SomeWhiteSpace(),
				DateOperators(),
				SomeWhiteSpace(),
				PeriodLiteral(),
				swap3(),
				push(
						new DateCalculationNode(
								(ValueNode<TemporalValue<?>>) pop(),
								(String) pop(),
								(ValueNode<PeriodValue>) pop()
						)
				)
		);
	}

	Rule DateFunction() {
		return Sequence(
				DateFunctionName(),
				"(",
				WhiteSpace(),
				DateExpression(),
				WhiteSpace(),
				",",
				WhiteSpace(),
				DateExpression(),
				WhiteSpace(),
				",",
				WhiteSpace(),
				ChronoUnit(),
				WhiteSpace(),
				")",
				swap3(),
				push(
						new DateFunctionNode(
								(ValueNode<TemporalValue<?>>) pop(),
								(ValueNode<TemporalValue<?>>) pop(),
								(String) pop()
						)
				)
		);
	}

	Rule DateFunctionName() {
		return Sequence(
				"RELATIVE",
				push(match())
		);
	}

	Rule DateOperators() {
		return Sequence(
				FirstOf(
						"ADD",
						"MIN"
				),
				push(match())
		);
	}

	Rule IntLiteralOfN(int n) {
		return Sequence(
				NTimes(n, Digit()),
				push(new ConstantNode<IntValue>(new IntValue(Integer.parseInt(match()))))
		);
	}

	Rule DateLiteral() {
		return Sequence(
				"#",
				DateBody(),
				"#",
				swap3(),
				push(
						new DateNode(
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop()
						)
				)
		);
	}

	Rule DateBody() {
		return Sequence(
				IntLiteralOfN(4),
				"-",
				IntLiteralOfN(2),
				"-",
				IntLiteralOfN(2)
		);
	}

	Rule DateComparison() {
		return Sequence(
				DateExpression(),
				SomeWhiteSpace(),
				Sequence(
						FirstOf("AFTER", "BEFORE"),
						push(match())
				),
				SomeWhiteSpace(),
				DateExpression(),
				swap3(),
				push(new DateCompareNode(
								(ValueNode<? extends TemporalValue<?>>) pop(),
								(String) pop(),
								(ValueNode<? extends TemporalValue<?>>) pop()
						)
				)
		);
	}

	Rule TimeBody() {
		return Sequence(
				IntLiteralOfN(2),
				":",
				IntLiteralOfN(2),
				FirstOf(
						Sequence(
								":",
								IntLiteralOfN(2)
						),
						Sequence(
								TestNot(
										Sequence(
												":",
												IntLiteralOfN(2)
										)
								),
								push(new ConstantNode<IntValue>(new IntValue(0)))
						)

				)
		);
	}

	Rule DateTimeLiteral() {
		return Sequence(
				"#",
				DateBody(),
				" ",
				TimeBody(),
				"#",
				swap6(),
				push(new DateTimeNode(
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop(),
								(ValueNode<IntValue>) pop()
						)
				)
		);
	}

	Rule DateColumn() {
		return Sequence(
				ColumnIdentifier(),
				push(new TableValueNode<TemporalValue>((ColumnIdentifier) pop()))
		);
	}

	Rule Variable() {
		return FirstOf(
				ColumnIdentifier(),
				Identifier(),
				StringLiteral(),
				FloatLiteral(),
				IntLiteral()
		);
	}

	/**
	 * Matches a set of parameters and returns an ArrayList of them.
	 */
	Rule Params() {
		return Sequence(
				push(new ArrayList<Object>()),
				"(",
				Optional(ParamDecl()),
				")"
		);
	}

	Rule ParamDecl() {
		Var<List<Object>> paramList = new Var<List<Object>>();
		return Sequence(
				paramList.set((List<Object>) pop()),
				Variable(),
				paramList.get().add(pop()),
				push(paramList.get()),
				ParamRest());
	}

	Rule ParamRest() {
		return Optional(Sequence(",", WhiteSpace(), ParamDecl()));
	}

	Rule WhiteSpace() {
		return ZeroOrMore(WhiteSpaceChars());
	}

	Rule SomeWhiteSpace() {
		return OneOrMore(WhiteSpaceChars());
	}

	Rule WhiteSpaceChars() {
		return FirstOf(" ", "\t", "\n");
	}

	/**
	 * Matches a process and pushes a ProcessInfo on the stack.
	 */
	Rule Process() {
		Var<Identifier> processName = new Var<Identifier>();
		Var<List<Object>> params = new Var<List<Object>>();
		return Sequence(
				Identifier(),
				processName.set((Identifier) pop()),
				Params(),
				params.set((List<Object>) pop()),
				push(new ProcessInfo(processName.get(), params.get().toArray()))
		);
	}

	/**
	 * Matches the main process chain.
	 */
	Rule Pipe() {
		return Sequence(
				Process(),
				Optional(
						"|",
						FirstOf(
								Pipe(),
								Process()
						)
				),
				TestNot("|")
		);
	}

	Rule Digit() {
		return CharRange('0', '9');
	}

	/**
	 * Matches table.column and pushes a ColumnIdentifier on the stack
	 */
	Rule ColumnIdentifier() {
		return Sequence(
				Identifier(),
				".",
				Identifier(),
				swap(),
				push(new ColumnIdentifier((Identifier) pop(), (Identifier) pop()))
		);
	}

	Rule CompareOperator() {
		return Sequence(
				FirstOf("<=", ">=", ">", "<"),
				push(match())
		);
	}

	/**
	 * Matches any comparison and pushes a CompareNode on the stack.
	 */
	Rule Comparison() {
		return FirstOf(
				DateComparison(),
				NumberComparison()
		);
	}

	Rule NumberComparison() {
		return Sequence(
				NumberExpression(),
				WhiteSpace(),
				CompareOperator(),
				WhiteSpace(),
				NumberExpression(),
				swap3(),
				push(new CompareNode((ValueNode) pop(), (String) pop(), (ValueNode) pop()))
		);
	}

	Rule Equality() {
		return Sequence(
				NumberExpression(),
				WhiteSpace(),
				"=",
				WhiteSpace(),
				NumberExpression(),
				swap(),
				push(new EqualityNode((ValueNode) pop(), (ValueNode) pop()))
		);
	}

	Rule AnyValue() {
		return FirstOf(
				StringExpression(),
				NumberExpression(),
				DateExpression(),
				BooleanTerm()
		);
	}

	Rule BooleanColumn() {
		return Sequence(
				ColumnIdentifier(),
				push(new TableValueNode<BoolValue>((ColumnIdentifier) pop()))
		);
	}

	Rule BooleanExpression() {
		return FirstOf(
				BooleanOperation(),
				Comparison(),
				Equality(),
				UnaryBooleanOperation(),
				BooleanLiteral(),
				Sequence("(", BooleanExpression(), ")"),
				BooleanColumn()
		);
	}

	Rule BooleanLiteral() {
		return Sequence(
				FirstOf("true", "false"),
				push(new ConstantNode<BoolValue>(new BoolValue(Boolean.parseBoolean(match()))))
		);
	}

	Rule UnaryBooleanOperation() {
		return FirstOf(
				NotOperation(),
				CodeCheck()
		);
	}

	Rule CodeCheck() {
		return Sequence(
				"HAS_CODE(",
				StringExpression(),
				")",
				push(new CodeCheckNode((ValueNode<StringValue>) pop()))
		);
	}

	Rule StringColumn() {
		return Sequence(
				ColumnIdentifier(),
				push(new TableValueNode<StringValue>((ColumnIdentifier) pop()))
		);
	}

	Rule StringExpression() {
		return FirstOf(
				StringLiteral(),
				StringColumn()
		);
	}

	Rule NotOperation() {
		return Sequence(
				"NOT(",
				BooleanExpression(),
				")",
				push(new BooleanOperationNode((ValueNode<BoolValue>) pop(), "NOT", null))
		);
	}

	/**
	 * Matches the AND and OR operations and pushes a ValueNode<BoolValue> on the stack.
	 */
	Rule BooleanOperation() {
		return Sequence(
				BooleanTerm(),
				WhiteSpace(),
				BooleanOperator(),
				WhiteSpace(),
				BooleanTerm(),
				swap3(),
				push(new BooleanOperationNode(
						(ValueNode<BoolValue>) pop(),
						(String) pop(),
						(ValueNode<BoolValue>) pop()))
		);
	}

	Rule BooleanTerm() {
		return FirstOf(
				BooleanLiteral(),
				Comparison(),
				Equality(),
				UnaryBooleanOperation(),
				Sequence("(", BooleanExpression(), ")")
		);
	}

	Rule BooleanOperator() {
		return Sequence(
				FirstOf("OR", "AND"),
				push(match())
		);
	}

	/**
	 * The main entry point for our language.
	 */
	Rule Sugar() {
		return Sequence(
				ZeroOrMore(Macro()),
				WhiteSpace(),
				Pipe()
		);
	}

	/**
	 * Matches a macro definition and pushes a MacroInfo on the stack.
	 */
	Rule Macro() {
		return Sequence(
				"def",
				WhiteSpace(),
				Identifier(),
				WhiteSpace(),
				":",
				WhiteSpace(),
				MacroType(),
				WhiteSpace(),
				"=",
				WhiteSpace(),
				MacroBody(),
				";",
				swap3(),
				push(
						new MacroInfo(
								(Identifier) pop(),
								new MacroType((String) pop()),
								(String) pop()
						)
				)
		);
	}

	Rule MacroBody() {
		return Sequence(OneOrMore(TestNot(AnyOf(";")), ANY), push(match()));
	}

	Rule MacroType() {
		return Sequence(
				Sequence(CharRange('A', 'Z'), ZeroOrMore(SafeCharacter())),
				push(match())
		);
	}


	Rule GroupBy(Rule selector) {
		return Sequence(
				"NAME",
				SomeWhiteSpace(),
				Identifier(),
				SomeWhiteSpace(),
				"ON",
				SomeWhiteSpace(),
				selector,
				GroupByFunctions()
		);
	}

	Rule GroupByColumn() {
		return Sequence(
				GroupBy(
						Sequence(
								AnyValue(),
								TestNot(
										WhiteSpace(),
										"AS",
										WhiteSpace()
								)
						)
				),
				swap4()
		);
	}

	Rule GroupByConstraints() {
		return Sequence(
				GroupBy(
						GroupByConstraintsList()
				),
				swap5()
		);
	}

	Rule GroupByConstraintsList() {
		Var<List<ValueNode<BoolValue>>> constraints = new Var<>();
		Var<List<Identifier>> groupNames = new Var<>();
		return Sequence(
				new Action() {
					@Override
					public boolean run(Context context) {
						constraints.set(new ArrayList<>());
						groupNames.set(new ArrayList<>());
						return true;
					}
				},
				ZeroOrMore(
						GroupByConstraintConstraint(),
						",",
						WhiteSpace(),
						groupNames.get().add((Identifier) pop()),
						constraints.get().add((ValueNode<BoolValue>) pop())
				),
				Optional(
						GroupByConstraintConstraint(),
						groupNames.get().add((Identifier) pop()),
						constraints.get().add((ValueNode<BoolValue>) pop())
				),
				push(constraints.get()),
				push(groupNames.get())
		);
	}

	Rule GroupByConstraintConstraint() {
		return Sequence(
				BooleanExpression(),
				SomeWhiteSpace(),
				"AS",
				SomeWhiteSpace(),
				Identifier()
		);
	}

	Rule GroupByFunctions() {
		Var<List<FunctionNode>> functions = new Var<>();
		Var<List<Identifier>> names = new Var<>();
		return Sequence(
				new Action() {
					@Override
					public boolean run(Context context) {
						functions.set(new ArrayList<>());
						names.set(new ArrayList<>());
						return true;
					}
				},
				Optional(
						SomeWhiteSpace(),
						"FROM",
						SomeWhiteSpace(),
						// This rather unusual structure is required, as a recursive approach would
						// result in a StackOverflowException and since GroupByFunction is matched
						// before the comma the values have to be added after the comma.
						// (otherwise they'd be added twice.)
						ZeroOrMore(
								GroupByFunction(),
								",",
								WhiteSpace(),
								names.get().add((Identifier) pop()),
								functions.get().add((FunctionNode) pop())
						),
						GroupByFunction(),
						names.get().add((Identifier) pop()),
						functions.get().add((FunctionNode) pop())
				),
				push(functions.get()),
				push(names.get())
		);
	}

	Rule GroupByFunction() {
		return Sequence(
				TableFunction(),
				SomeWhiteSpace(),
				"AS",
				SomeWhiteSpace(),
				Identifier(),
				WhiteSpace()
		);
	}

	Rule Join() {
		return Sequence(
				JoinType(),
				SomeWhiteSpace(),
				Identifier(),
				SomeWhiteSpace(),
				"WITH",
				SomeWhiteSpace(),
				Identifier(),
				SomeWhiteSpace(),
				"AS",
				SomeWhiteSpace(),
				Identifier(),
				JoinColumns(),
				swap6()
		);
	}

	Rule JoinType() {
		return Sequence(
				FirstOf(
						"FULL JOIN",
						"LEFT JOIN",
						"RIGHT JOIN",
						"JOIN"
				),
				push(match())
		);
	}

	Rule JoinColumns() {
		Var<List<ColumnIdentifier>> oldColumns = new Var<>();
		Var<List<ColumnIdentifier>> newColumns = new Var<>();
		return Sequence(
				new Action() {
					@Override
					public boolean run(Context context) {
						oldColumns.set(new ArrayList<>());
						newColumns.set(new ArrayList<>());
						return true;
					}
				},
				Optional(
						SomeWhiteSpace(),
						"FROM",
						SomeWhiteSpace(),
						ZeroOrMore(
								Sequence(
										JoinColumn(),
										",",
										WhiteSpace(),
										newColumns.get().add((ColumnIdentifier) pop()),
										oldColumns.get().add((ColumnIdentifier) pop())
								)
						),
						JoinColumn(),
						newColumns.get().add((ColumnIdentifier) pop()),
						oldColumns.get().add((ColumnIdentifier) pop())
				),
				push(oldColumns.get()),
				push(newColumns.get())
		);
	}

	Rule JoinColumn() {
		return Sequence(
				ColumnIdentifier(),
				SomeWhiteSpace(),
				"AS",
				SomeWhiteSpace(),
				ColumnIdentifier()
		);
	}
}
