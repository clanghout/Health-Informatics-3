package model.process.describer;

import model.data.Row;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

/**
 * The describer for values that don't change.
 *
 * Created by Boudewijn on 11-5-2015.
 * @param <Type> The type you want this constant to be
 */
public final class ConstantDescriber<Type extends DataValue> extends DataDescriber<Type> {

	private Type constant;

	/**
	 * Construct a new ConstantDescriber.
	 * @param constant The constant you want to describe
	 */
	public ConstantDescriber(Type constant) {
		this.constant = constant;
	}

	/**
	 * Resolves the constant described.
	 * @param row This value is ignored, only required for compatibility
	 *               with DataDescriber#resolve()
	 * @return The constant described
	 */
	@Override
	public Type resolve(Row row) {
		return constant;
	}

	public static ConstantDescriber<DataValue> resolveType(Object value) {
		if (value instanceof Integer) {
			return new ConstantDescriber<>(new IntValue((Integer) value));
		} else if (value instanceof Float) {
			return new ConstantDescriber<>(new FloatValue((Float) value));
		} else if (value instanceof String) {
			return new ConstantDescriber<>(new StringValue((String) value));
		} else {
			throw new UnsupportedOperationException("This type has not yet been implemented");
		}
	}
}
