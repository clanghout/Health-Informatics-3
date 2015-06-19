package model.data.value;

/**
 * Data Class containing a value with type String.
 */
public class StringValue extends DataValue<String> {
	private String value;

	/**
	 * Return a null instance.
	 */
	StringValue() {
		this(null);
	}

	public StringValue(String value) {
		if (value == null) {
			this.value = "";
			setNull(true);
		} else {
			this.value = value;
		}
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toStringNotNull() {
		return value;
	}

	@Override
	public boolean doEquals(Object obj) {
		return ((StringValue) obj).value.equals(value);
	}

	@Override
	public int doHashCode() {
		return value.hashCode();
	}

	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof StringValue)) {
			throw new IllegalArgumentException("IntValue cannot compare to non ints");
		}
		StringValue o = (StringValue) other;
		return value.compareTo(o.value);
	}
}
