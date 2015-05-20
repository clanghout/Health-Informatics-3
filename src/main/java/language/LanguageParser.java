package language;

import model.data.process.DataProcess;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

/**
 * The parser for the analysis language.
 *
 * Since this class is rather declarative in its working, the style deviates from the default
 * Java style and is more PEG based.
 *
 * Created by Boudewijn on 20-5-2015.
 */
@SuppressWarnings("ALL")
public final class LanguageParser extends BaseParser<DataProcess> {

	Rule Identifier() {
		return Sequence(
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
		);
	}

	Rule StringLiteral() {
		return Sequence(
				"\"",
				ZeroOrMore(Character()),
				"\"");
	}

	Rule Character() {
		return NormalCharacter();
	}

	Rule NormalCharacter() {
		return Sequence(TestNot(AnyOf("\"")), ANY);
	}

	Rule IntLiteral() {
		return OneOrMore(Digit());
	}

	Rule FloatLiteral() {
		return Sequence(
				OneOrMore(Digit()),
				".",
				OneOrMore(Digit())
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
