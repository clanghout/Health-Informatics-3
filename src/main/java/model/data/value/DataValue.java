package model.data.value;


/**
 * Abstract class DataValue describing the DataValue objects.
 * @param <Type> return type of getValue()
 */
public abstract class DataValue<Type> {
	private boolean isNull;
	
	public abstract Type getValue();
	public abstract String toString();
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataValue)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		if (isNull || ((DataValue) obj).isNull()) {
			return false;
		}
		return doEquals(obj);
	}

	@Override
	public abstract int hashCode();
	
	public boolean isNull() {
		return isNull;
	}
	
	protected void setNull(boolean val) {
		isNull = val;
	}
	
	protected abstract boolean doEquals(Object obj);
}
