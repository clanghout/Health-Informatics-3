package model.data.value;

import model.data.DataValue;

/**
 * Data Class containing a value with type Int.
 */
public class IntValue extends DataValue<Integer> {
	private int value;

	public IntValue(Integer value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntValue)) {
			return false;
		}
		IntValue other = (IntValue) obj;
		return other.value == this.value;
	}

	@Override
	public int hashCode() {
		return value;
	}
}
