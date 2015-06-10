package model.data.value;

/**
 * Data Class containing a value with type Float.
 */
public final class FloatValue extends NumberValue<Float> {
	private float value;

	public FloatValue(Float value) {
		if (value == null) {
			this.value = 0.0f;
			setNull(true);
		} else {
			this.value = value;
		}
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
	public boolean doEquals(Object obj) {
		return ((FloatValue) obj).value == this.value;
	}

	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof FloatValue)) {
			throw new IllegalArgumentException(
					"FloatValue cannot compare to non floats");
		}
		FloatValue o = (FloatValue) other;
		return Float.compare(value, o.value);
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(value);
	}
}
