package model.data.value;

/**
 * Data Class containing a value with type Float.
 */
public class FloatValue extends DataValue<Float> {
	private float value;

	public FloatValue(float value) {
		this.value = value;
	}

	@Override
	public Float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FloatValue)) {
			return false;
		}
		FloatValue other = (FloatValue) obj;
		return other.value == this.value;
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(value);
	}
}
