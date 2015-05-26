package language;


import model.data.DataModel;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.process.analysis.operations.constraints.*;
import model.data.value.BoolValue;

/**
 * Created by Boudewijn on 26-5-2015.
 */
class BooleanOperationNode extends BooleanNode {

	private String operation;

	BooleanOperationNode(BooleanNode left, String operation, BooleanNode right) {
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

	DataDescriber<BoolValue> resolve(DataModel model) {
		return new ConstraintDescriber(resolveCheck(model, operation));
	}
}
