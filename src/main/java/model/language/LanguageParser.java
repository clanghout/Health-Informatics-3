package model.language;

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
				push(new NumberOperationNode((NumberNode) pop(), (String) pop(), (NumberNode) pop()))
		);
	}

	Rule Sqrt() {
		return Sequence(
				"SQRT(",
				NumberExpression(),
				")",
				push(new NumberOperationNode((NumberNode) pop(), "SQRT", null))
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
	 * Matches a column and pushes a TableNumberNode on the stack.
	 */
	Rule NumberColumn() {
		return Sequence(
				ColumnIdentifier(),
				push(new TableNumberNode((ColumnIdentifier) pop()))
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
				"\""
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
				push(new NumberConstantNode(Integer.parseInt(matchOrDefault("0"))))
		);
	}

	Rule FloatLiteral() {
		return Sequence(
				Sequence(
						OneOrMore(Digit()),
						".",
						OneOrMore(Digit())
				),
				push(new NumberConstantNode(Float.parseFloat(matchOrDefault("0.0"))))
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
				FirstOf("<=", ">=", "=", ">", "<"),
				push(match())
		);
	}

	/**
	 * Matches any comparison and pushes a CompareNode on the stack.
	 */
	Rule Comparison() {
		return Sequence(
				FirstOf(
						BooleanLiteral(),
						NotOperation(),
						Sequence("(", BooleanExpression(), ")"),
						NumberExpression()),
				WhiteSpace(),
				CompareOperator(),
				WhiteSpace(),
				FirstOf(
						BooleanLiteral(),
						NotOperation(),
						Sequence("(", BooleanExpression(), ")"),
						NumberExpression()),
				swap3(),
				push(new CompareNode(pop(), (String) pop(), pop()))
		);
	}

	Rule BooleanColumn() {
		return Sequence(
				ColumnIdentifier(),
				push(new TableBooleanNode((ColumnIdentifier) pop()))
		);
	}

	Rule BooleanExpression() {
		return FirstOf(
				NotOperation(),
				BooleanOperation(),
				Comparison(),
				BooleanLiteral(),
				Sequence("(", BooleanExpression(), ")"),
				BooleanColumn()
		);
	}

	Rule BooleanLiteral() {
		return Sequence(
				FirstOf("true", "false"),
				push(new BoolConstantNode(Boolean.parseBoolean(match())))
		);
	}

	Rule NotOperation() {
		return Sequence(
				"NOT(",
				BooleanExpression(),
				")",
				push(new BooleanOperationNode((BooleanNode) pop(), "NOT", null))
		);
	}

	/**
	 * Matches the AND and OR operations and pushes a BooleanOperationNode on the stack.
	 */
	Rule BooleanOperation() {
		return Sequence(
				FirstOf(
						BooleanLiteral(),
						Comparison(),
						NotOperation(),
						Sequence("(", BooleanExpression(), ")")),
				WhiteSpace(),
				BooleanOperator(),
				WhiteSpace(),
				FirstOf(
						BooleanLiteral(),
						Comparison(),
						NotOperation(),
						Sequence("(", BooleanExpression(), ")")),
				swap3(),
				push(new BooleanOperationNode(
						(BooleanNode) pop(),
						(String) pop(),
						(BooleanNode) pop()))
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
