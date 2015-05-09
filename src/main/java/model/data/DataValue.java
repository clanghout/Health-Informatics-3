package model.data;


/**
 * Abstract class DataValue describing the DataValue objects.
 */
public abstract class DataValue {

	public abstract String getValue();
	public abstract String toString();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();
}
