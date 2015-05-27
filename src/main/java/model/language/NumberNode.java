package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;
import org.parboiled.trees.ImmutableBinaryTreeNode;

/**
 * Created by Boudewijn on 27-5-2015.
 */
abstract class NumberNode extends ImmutableBinaryTreeNode<NumberNode> {

	NumberNode(NumberNode left, NumberNode right) {
		super(left, right);
	}

	abstract DataDescriber<NumberValue> resolve(DataModel model);
}