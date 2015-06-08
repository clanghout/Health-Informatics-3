package model.data.value;

/**
 * Data Class containing a value with type Float.
 */
public final class FloatValue extends NumberValue<Float> {
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
	public int compareTo(NumberValue other) {
		if (!(other instanceof FloatValue)) {
			throw new IllegalArgumentException("FloatValue cannot compare to non floats");
		}
		FloatValue o = (FloatValue) other;
		return Float.compare(value, o.value);
	}

	@Override
	public FloatValue copy() {
		return new FloatValue(value);
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(value);
	}
}
