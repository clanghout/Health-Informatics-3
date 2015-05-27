package model.language;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * Created by Boudewijn on 27-5-2015.
 */
class NumberConstantNode extends NumberNode {

	private final DataDescriber<NumberValue> value;
	private final Number number;

	NumberConstantNode(Number number) {
		super(null, null);

		value = resolveType(number);
		this.number = number;
	}

	private DataDescriber<NumberValue> resolveType(Number number) {
		if (number instanceof Float) {
			return new ConstantDescriber<>(new FloatValue((Float) number));
		} else if (number instanceof Integer) {
			return new ConstantDescriber<>(new IntValue((Integer) number));
		} else {
			throw new IllegalArgumentException(String.format("Number %s not float or int", number));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		NumberConstantNode that = (NumberConstantNode) o;

		return number.equals(that.number);
	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}

	DataDescriber<NumberValue> resolve(DataModel model) {
		return value;

	}
}
