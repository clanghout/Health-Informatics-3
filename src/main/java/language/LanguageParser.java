package language;

import org.parboiled.BaseParser;
import org.parboiled.Rule;

/**
 * The parser for the analysis language.
 * <p/>
 * Since this class is rather declarative in its working, the style deviates from the default
 * Java style and is more PEG based.
 * <p/>
 * Created by Boudewijn on 20-5-2015.
 */
@SuppressWarnings("ALL")
public class LanguageParser extends BaseParser<Object> {

	Rule Identifier() {
		return Sequence(
				Sequence(
						FirstOf(
								CharRange('a', 'z'),
								CharRange('A', 'Z')
						), OneOrMore(
								FirstOf(
										Digit(),
										CharRange('a', 'z'),
										CharRange('A', 'Z')
								)
						)
				),
				push(new Identifier<>(match()))
		);
	}

	Rule StringLiteral() {
		return Sequence(
				Sequence(
						"\"",
						ZeroOrMore(Character()),
						"\""),
				push(matchOrDefault(""))
		);
	}

	Rule Character() {
		return NormalCharacter();
	}

	Rule NormalCharacter() {
		return Sequence(TestNot(AnyOf("\"")), ANY);
	}

	Rule IntLiteral() {
		return Sequence(
				OneOrMore(Digit()),
				push(Integer.parseInt(matchOrDefault("0")))
		);
	}

	Rule FloatLiteral() {
		return Sequence(
				Sequence(
						OneOrMore(Digit()),
						".",
						OneOrMore(Digit())
				),
				push(Float.parseFloat(matchOrDefault("0.0")))
		);
	}

	Rule Variable() {
		return FirstOf(Identifier(), StringLiteral(), FloatLiteral(), IntLiteral());
	}

	Rule Params() {
		return Sequence(
				"(",
				Optional(ParamDecl()),
				")"
		);
	}

	Rule ParamDecl() {
		return Sequence(Variable(), ParamRest());
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

	Rule Process() {
		return Sequence(Identifier(), Params());
	}

	Rule Pipe() {
		return Sequence(
				Process(),
				"|",
				FirstOf(
						Pipe(),
						Process()
				)
		);
	}

	Rule Digit() {
		return CharRange('0', '9');
	}
}
