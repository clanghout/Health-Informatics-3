package model.language;

import model.data.DataModel;
import model.data.describer.ComputationDescriber;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;
import model.process.analysis.operations.computations.*;

/**
 * Created by Boudewijn on 27-5-2015.
 */
class NumberOperationNode extends NumberNode {

	private final String operation;

	NumberOperationNode(NumberNode left, String operation, NumberNode right) {
		super(left, right);

		this.operation = operation;
	}

	private Computation resolveComputation(DataModel model) {
		switch (operation) {
			case "+": return new Addition<>(left().resolve(model), right().resolve(model));
			case "-": return new Subtraction<>(left().resolve(model), right().resolve(model));
			case "*": return new Multiplication<>(left().resolve(model), right().resolve(model));
			case "/": return new Division<>(left().resolve(model), right().resolve(model));
			case "^": return new Power<>(left().resolve(model), right().resolve(model));
			case "SQRT": return new SquareRoot<>(left().resolve(model));
			default:
				throw new UnsupportedOperationException(
						String.format("Operation %s not supported", operation)
				);
		}
	}

	DataDescriber<NumberValue> resolve(DataModel model) {
		return new ComputationDescriber<>(resolveComputation(model));
	}
}
