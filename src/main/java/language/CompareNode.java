package language;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.process.analysis.operations.constraints.Constraint;
import model.data.process.analysis.operations.constraints.EqualityCheck;
import model.data.value.DataValue;
import org.parboiled.trees.ImmutableBinaryTreeNode;

/**
 * This class represent a comparison.
 *
 * Created by Boudewijn on 21-5-2015.
 */
final class CompareNode extends ImmutableBinaryTreeNode<CompareNode> {

	private Object left;
	private Object right;
	private Character operator;

	CompareNode(Object left, Character operator, Object right) {
		super(null, null);
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	Constraint resolve(DataModel model) {
		DataDescriber<DataValue> leftSide = resolveNode(model, left);
		DataDescriber<DataValue> rightSide = resolveNode(model, right);

		return resolveConstraint(leftSide, operator, rightSide);
	}

	private DataDescriber<DataValue> resolveNode(DataModel model, Object node) {
		if (node instanceof ColumnIdentifier) {
			ColumnIdentifier columnIdentifier = (ColumnIdentifier) node;

			return new RowValueDescriber<>(
					model.getByName(
							columnIdentifier.getTable()
					).getColumn(columnIdentifier.getColumn()));
		} else {
			return ConstantDescriber.resolveType(node);
		}
	}

	private Constraint resolveConstraint(
			DataDescriber<DataValue> left,
			Character operator,
			DataDescriber<DataValue> right) {
		switch (operator) {
			case '=': return new EqualityCheck<>(left, right);
			default: throw new UnsupportedOperationException("Constraint not yet implemented");
		}
	}
}