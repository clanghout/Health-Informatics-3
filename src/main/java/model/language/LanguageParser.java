package model.language;

import model.data.value.*;
import model.language.nodes.*;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.Var;

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

	Rule DateExpression() {
		return FirstOf(
				DateOperation(),
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

	Rule DateOperation() {
		return Sequence(
				DateTerm(),
				OneOrMore(WhiteSpaceChars()),
				DateOperators(),
				OneOrMore(WhiteSpaceChars()),
				PeriodLiteral(),
				swap3(),
				push(
						new DateOperationNode(
								(ValueNode<TemporalValue<?>>) pop(),
								(String) pop(),
								(ValueNode<PeriodValue>) pop()
						)
				)
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
				OneOrMore(WhiteSpaceChars()),
				Sequence(
						FirstOf("AFTER", "BEFORE"),
						push(match())
				),
				OneOrMore(WhiteSpaceChars()),
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
		return FirstOf(Identifier(), StringLiteral(), FloatLiteral(), IntLiteral());
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

	Rule WhiteSpaceChars() {
		return FirstOf(" ", "\t");
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
				)
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
		return ZeroOrMore(
				FirstOf(
						Macro(),
						Pipe(),
						ANY
				)
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
				Params(),
				WhiteSpace(),
				":",
				WhiteSpace(),
				MacroType(),
				WhiteSpace(),
				"=",
				WhiteSpace(),
				MacroBody(),
				";",
				swap4(),
				push(
						new MacroInfo(
								(Identifier) pop(),
								(List<Object>) pop(),
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

}
