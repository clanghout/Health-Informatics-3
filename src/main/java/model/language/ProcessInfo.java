package model.language;

import model.data.describer.DataDescriber;
import model.process.DataProcess;
import model.process.FromProcess;
import model.process.IsProcess;
import model.process.analysis.ConstraintAnalysis;

import java.util.Map;

/**
 * Contains the information required to produce a DataProcess.
 *
 * Created by Boudewijn on 20-5-2015.
 */
class ProcessInfo {

	private Identifier<Object> name;
	private Object[] parameters;

	ProcessInfo(Identifier<Object> name, Object[] parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	Object[] getParameters() {
		return parameters;
	}

	Identifier<Object> getIdentifier() {
		return name;
	}

	DataProcess resolve(Map<Identifier, DataDescriber> macros) {
		switch (name.getName()) {
			case "from":
				return new FromProcess((Identifier) parameters[0]);
			case "is":
				return new IsProcess((Identifier) parameters[0]);
			case "constraint":
				return new ConstraintAnalysis((DataDescriber) macros.get(parameters[0]));
			default:
				throw new UnsupportedOperationException("This code has not been implemented yet");
		}
	}
}
