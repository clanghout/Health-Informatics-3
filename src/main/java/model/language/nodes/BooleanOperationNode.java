package model.language.nodes;


import model.data.DataModel;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.process.analysis.operations.constraints.*;
import model.data.value.BoolValue;

/**
 * Created by Boudewijn on 26-5-2015.
 */
public class BooleanOperationNode extends ValueNode<BoolValue> {

	private String operation;

	public BooleanOperationNode(
			ValueNode<BoolValue> left,
			String operation,
			ValueNode<BoolValue> right) {
		super(left, right);
		this.operation = operation;
	}

	private Constraint resolveCheck(
			DataModel model,
			String operation) {
		switch (operation) {
			case "AND":	return new AndCheck(left().resolve(model), right().resolve(model));
			case "OR": return new OrCheck(left().resolve(model), right().resolve(model));
			case "NOT": return new NotCheck(left().resolve(model));
			default: throw new IllegalArgumentException(
					String.format("Operation %s is not a valid boolean operation", operation)
			);
		}
	}

	public DataDescriber<BoolValue> resolve(DataModel model) {
		return new ConstraintDescriber(resolveCheck(model, operation));
	}
}
