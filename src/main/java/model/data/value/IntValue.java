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
//		if (!(obj instanceof IntValue)) {
//			return false;
//		}
		IntValue other = (IntValue) obj;
		return other.value == this.value;
	}

	@Override
	public int compareTo(NumberValue other) {
		if (!(other instanceof IntValue)) {
			throw new IllegalArgumentException("IntValue cannot compare to non ints");
		}
		IntValue o = (IntValue) other;
		return Integer.compare(value, o.value);
	}

	@Override
	public int hashCode() {
		return value;
	}
}
