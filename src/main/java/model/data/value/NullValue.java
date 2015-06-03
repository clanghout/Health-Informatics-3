package model.data.value;

/**
 * Created by Chris on 3-6-2015.
 */
public class NullValue extends DataValue {

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

	@Override
	public DataValue copy() {
		return new NullValue();
	}
}
