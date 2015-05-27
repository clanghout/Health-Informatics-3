package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import org.parboiled.trees.ImmutableBinaryTreeNode;

/**
 * Created by Boudewijn on 26-5-2015.
 */
abstract class BooleanNode extends ImmutableBinaryTreeNode<BooleanNode> {

	BooleanNode(
			BooleanNode left,
			BooleanNode right) {
		super(left, right);
	}

	abstract DataDescriber<BoolValue> resolve(DataModel model);
}
