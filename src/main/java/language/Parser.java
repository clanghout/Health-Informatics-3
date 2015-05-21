package language;

import model.data.DataModel;
import model.data.process.DataProcess;
import model.data.process.SerialProcess;
import model.data.process.analysis.operations.Operation;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import java.util.ArrayList;
import java.util.Collections;
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
	public DataProcess parse(String input, DataModel model) {
		LanguageParser parser = Parboiled.createParser(LanguageParser.class);
		BasicParseRunner runner = new BasicParseRunner(parser.Sugar());

		ParsingResult result = runner.run(input);
		List<DataProcess> processes = new ArrayList<>();
		List<Operation> macros = new ArrayList<>();

		while (!result.valueStack.isEmpty()) {
			Object info = result.valueStack.pop();
			if (info instanceof ProcessInfo) {
				ProcessInfo processInfo = (ProcessInfo) info;
				processes.add(processInfo.resolve());
			} else if (info instanceof MacroInfo) {
				macros.add(((MacroInfo) info).parse(model));
			} else {
				throw new UnsupportedOperationException("Not yet implemented");
			}
		}

		Collections.reverse(processes);

		SerialProcess process = new SerialProcess(processes);
		process.setDataModel(model);
		return process;
	}
}
