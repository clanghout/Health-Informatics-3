package language;

import model.data.process.DataProcess;
import model.data.process.SerialProcess;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import java.util.ArrayList;
import java.util.List;

/**
 * The parser for SUGAR.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class Parser {

	public Parser() {

	}

	/**
	 * Parse the given string into the required DataProcesses.
	 * @param input The SUGAR code.
	 * @return The DataProcesses parsed from the given string.
	 */
	public DataProcess parse(String input) {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);
		BasicParseRunner runner = new BasicParseRunner(parser.Pipe());

		ParsingResult result = runner.run(input);
		List<DataProcess> processes = new ArrayList<>();

		while (!result.valueStack.isEmpty()) {
			ProcessInfo info = (ProcessInfo) result.valueStack.pop();
			processes.add(info.resolve());
		}

		return new SerialProcess(processes);
	}
}
