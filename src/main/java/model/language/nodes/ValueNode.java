package model.language.nodes;

import model.data.DataModel;
import model.process.describer.DataDescriber;
import model.data.value.DataValue;
import org.parboiled.trees.ImmutableBinaryTreeNode;

/**
 * This class represents a node on which the parser can base its calculation tree.
 *
 * Created by Boudewijn on 4-6-2015.
 * @param <T> The type of value contained in this tree
 */
public abstract class ValueNode<T extends DataValue> extends
		ImmutableBinaryTreeNode<ValueNode<T>> {

	/**
	 * Construct a new ValueNode.
	 * @param left The left side leaf of this node.
	 * @param right The right side leaf of this node.
	 */
	public ValueNode(ValueNode<T> left, ValueNode<T> right) {
		super(left, right);
	}

	/**
	 * Resolves the value contained in this node to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the value as contained in this node.
	 */
	public abstract DataDescriber<T> resolve(DataModel model);
}
