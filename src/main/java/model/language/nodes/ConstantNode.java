package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;

/**
 * Represents a oonstant DataValue.
 *
 * Created by Boudewijn on 4-6-2015.
 * @param <T> The type of DataValue you want to contain in this node.
 */
public class ConstantNode<T extends DataValue> extends ValueNode<T> {

	private final T constant;

	/**
	 * Construct a new ConstantNode.
	 * @param constant The value you want to describe.
	 */
	public ConstantNode(T constant) {
		super(null, null);
		this.constant = constant;
	}

	/**
	 * Returns a DataDescriber describing this constant.
	 * @param model The model to be used.
	 * @return A DataDescriber describing this constant.
	 */
	@Override
	public DataDescriber<T> resolve(DataModel model) {
		return new ConstantDescriber<>(constant);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ConstantNode<?> that = (ConstantNode<?>) o;

		return constant.equals(that.constant);

	}

	@Override
	public int hashCode() {
		return constant.hashCode();
	}
}
