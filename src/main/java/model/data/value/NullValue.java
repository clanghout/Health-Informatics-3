package model.data.value;

/**
 * Data value for null.
 * Created by Chris on 3-6-2015.
 */
public class NullValue extends DataValue {

	/**
	 * Create a new NullValue.
	 */
	public NullValue() {
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NullValue;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}