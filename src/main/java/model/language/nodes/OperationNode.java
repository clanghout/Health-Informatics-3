package model.language.nodes;

import model.data.value.DataValue;

/**
 * Represents an operation.
 *
 * Created by Boudewijn on 4-6-2015.
 * @param <T> The type of DataValue this operation is applied to.
 */
public abstract class OperationNode<T extends DataValue> extends ValueNode<T> {

	private final String operation;

	/**
	 * Construct a new operation.
	 * @param left The left side operand of the operation.
	 * @param operation The operation itself.
	 * @param right The right side operand of the operation.
	 */
	public OperationNode(ValueNode<T> left, String operation, ValueNode<T> right) {
		super(left, right);
		this.operation = operation;
	}

	/**
	 * Get the operation.
	 * @return The operation.
	 */
	public String getOperation() {
		return operation;
	}
}
