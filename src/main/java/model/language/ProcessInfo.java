package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.StringValue;
import model.language.nodes.ValueNode;
import model.process.*;
import model.process.analysis.ConstraintAnalysis;
import model.process.analysis.GroupByAnalysis;
import model.process.setOperations.Difference;
import model.process.setOperations.Union;

import java.util.Arrays;
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

	DataProcess resolve(DataModel model, Map<Identifier, Object> macros) {
		switch (name.getName()) {
			case "from":
				Identifier[] identifiers = Arrays.stream(parameters)
						.map(x -> (Identifier) x)
						.toArray(Identifier[]::new);
				return new FromProcess(identifiers);
			case "is":
				if (parameters.length == 1) {
					return new IsProcess((Identifier) parameters[0]);
				} else if (parameters.length == 2) {
					return new DataTableIsProcess(
							(Identifier) parameters[0],
							(Identifier) parameters[1]);
				}
			case "constraint":
				return new ConstraintAnalysis((DataDescriber<BoolValue>) macros.get(parameters[0]));
			case "setCode":
				ValueNode<StringValue> stringNode = (ValueNode<StringValue>) parameters[0];
				DataDescriber<StringValue> code = stringNode.resolve(model);
				Identifier tableName = (Identifier) parameters[1];
				return new SetCode(code, tableName);
			case "difference":
				return new Difference((Identifier) parameters[0], (Identifier) parameters[1]);
			case "union":
				return new Union((Identifier) parameters[0], (Identifier) parameters[1]);
			case "groupBy":
				return (GroupByAnalysis) macros.get(parameters[0]);
			default:
				throw new UnsupportedOperationException("This code has not been implemented yet");
		}
	}
}
