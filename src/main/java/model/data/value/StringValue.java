package model.data.value;

/**
 * Data Class containing a value with type String.
 */
public class StringValue extends DataValue<String> {
	private String value;

	public StringValue(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StringValue)) {
			return false;
		}
		StringValue other = (StringValue) obj;
		return other.value.equals(value);
	}

	@Override
	public int hashCode() {
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
