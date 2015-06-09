package model.data.value;

import java.util.Comparator;


/**
 * Abstract class DataValue describing the DataValue objects.
 * @param <Type> return type of getValue()
 */
public abstract class DataValue<Type> implements Comparable<DataValue> {

	public abstract Type getValue();
	public abstract String toString();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();
}
