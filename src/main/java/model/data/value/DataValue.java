package model.data.value;


/**
 * Abstract class DataValue describing the DataValue objects.
 * @param <Type> return type of getValue()
 */
public abstract class DataValue<Type> {
	private boolean isNull = false;
	
	public abstract Type getValue();
	public abstract String toString();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();
	
	public boolean isNull() {
		return isNull;
	}
	
	public void setNull(boolean nul) {
		isNull = nul;
	}
}
