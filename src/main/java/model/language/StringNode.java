package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.value.StringValue;
import org.parboiled.trees.ImmutableBinaryTreeNode;

/**
 * Created by Boudewijn on 3-6-2015.
 */
abstract class StringNode extends ImmutableBinaryTreeNode<StringNode> {

	StringNode(StringNode left, StringNode right) {
		super(left, right);
	}

	abstract DataDescriber<StringValue> resolve(DataModel model);
}
