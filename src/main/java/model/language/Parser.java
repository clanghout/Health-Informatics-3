package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.process.DataProcess;
import model.process.SerialProcess;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import java.util.*;
import java.util.stream.Collectors;

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
		ParseRunner runner = new ReportingParseRunner<>(parser.Sugar());

		ParsingResult result = runner.run(input);
		List<ProcessInfo> processInfos = new ArrayList<>();
		Map<Identifier, DataDescriber> macros = new HashMap<>();

		while (!result.valueStack.isEmpty()) {
			Object info = result.valueStack.pop();
			if (info instanceof ProcessInfo) {
				ProcessInfo processInfo = (ProcessInfo) info;
				processInfos.add(processInfo);
			} else if (info instanceof MacroInfo) {
				MacroInfo macroInfo = (MacroInfo) info;
				macros.put(macroInfo.getIdentifier(), macroInfo.parse(model));
			} else {
				throw new UnsupportedOperationException("Not yet implemented");
			}
		}

		List<DataProcess> processes = processInfos.stream()
				.map(x -> x.resolve(macros))
				.collect(Collectors.toList());

		Collections.reverse(processes);

		SerialProcess process = new SerialProcess(processes);
		process.setDataModel(model);
		return process;
	}
}
