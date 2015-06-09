package model.data.value;



/**
 * Data Class containing a value with type Int.
 */
public final class IntValue extends NumberValue<Integer> {
	private int value;

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
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public boolean doEquals(Object obj) {
		return ((IntValue) obj).value == this.value;
	}

	@Override
	public int compareTo(NumberValue other) {
		return Integer.compare(value, ((IntValue) other).value);
	}

	@Override
	public int hashCode() {
		return value;
	}
}
