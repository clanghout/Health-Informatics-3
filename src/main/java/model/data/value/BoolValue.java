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
	public boolean equals(Object obj) {
		if (!(obj instanceof BoolValue)) {
			return false;
		}
		BoolValue other = (BoolValue) obj;
		return other.value == this.value;
	}

	@Override
	public int hashCode() {
		return Boolean.hashCode(value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
