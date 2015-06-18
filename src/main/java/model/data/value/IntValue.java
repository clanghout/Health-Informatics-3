package model.data.value;

/**
 * Data Class containing a value with type Int.
 */
public final class IntValue extends NumberValue<Integer> {
	private int value;

	/**
	 * Return a null instance.
	 */
	IntValue() {
		this(null);
	}

	public IntValue(Integer value) {
		if (value == null) {
			this.value = 0;
			setNull(true);
		} else {
			this.value = value;
		}
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String toStringNotNull() {
		return String.valueOf(value);
	}
	
	@Override
	public int doHashCode() {
		return value;
	}

	@Override
	public boolean doEquals(Object obj) {
		return ((IntValue) obj).value == this.value;
	}

	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof NumberValue)) {
			throw new IllegalArgumentException(
					"IntValue cannot compare to non numbers");
		}
		if (other instanceof IntValue) {
			IntValue o = (IntValue) other;
			return Integer.compare(value, o.value);
		} else {
			FloatValue o = (FloatValue) other;
			return o.compareTo(this);
		}
	}
}
