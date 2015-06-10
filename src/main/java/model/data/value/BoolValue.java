package model.data.value;

/**
 * A boolean DataValue.
 * Created by Boudewijn on 11-5-2015.
 */
public final class BoolValue extends DataValue<Boolean> {

	private boolean value;

	public BoolValue(Boolean value) {
		if (value == null) {
			this.value = false;
			setNull(true);
		} else {
			this.value = value;
		}
	}

	@Override
	public Boolean getValue() {
		return value;
	}
	
	@Override
	public boolean doEquals(Object obj) {
		return ((BoolValue) obj).value == this.value;
	}

	@Override
	public int hashCode() {
		return Boolean.hashCode(value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public int compareTo(DataValue other) {
		if (!(other instanceof BoolValue)) {
			throw new IllegalArgumentException("Boolvalue cannot compare to non bools");
		}
		BoolValue o = (BoolValue) other;
		return Boolean.compare(value, o.value);
	}
}
