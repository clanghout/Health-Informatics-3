package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.process.analysis.operations.constraints.EqualityCheck;

/**
 * Created by Boudewijn on 4-6-2015.
 * @param <T> The type of the values entered into this node.
 */
public final class EqualityNode<T extends DataValue> extends OperationNode<BoolValue> {

	private ValueNode<T> left;
	private ValueNode<T> right;

	/**
	 * Construct a new EqualityNode.
	 * @param left The left side operand of the equality operation.
	 * @param right The right side operand of the equality operation.
	 */
	public EqualityNode(
			ValueNode<T> left,
            ValueNode<T> right) {
		super(null, null, null);
		this.left = left;
		this.right = right;
	}

	/**
	 * Resolves the equality operation to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the equality check.
	 */
	@Override
	public DataDescriber<BoolValue> resolve(DataModel model) {
		return new ConstraintDescriber(
				new EqualityCheck<>(left.resolve(model), right.resolve(model))
		);
	}
}
