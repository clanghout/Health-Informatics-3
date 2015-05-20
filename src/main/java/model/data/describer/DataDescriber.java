package model.data.describer;

import model.data.DataRow;
import model.data.Row;
import model.data.value.DataValue;

/**
 * A class the describe a value.
 *
 * This can be used to abstract away if a value is contained in a DataRow or  if
 * it is a literal constant.
 *
 * Created by Boudewijn on 11-5-2015.
 * @param <Type> The type of the DataValue this DataDescriber is describing.
 */
public abstract class DataDescriber<Type extends DataValue> {

	/**
	 * Resolve the value described by the object.
	 * @param row The row the value should be resolved from
	 * @return The value described by this DataDescriber.
	 */
	public abstract Type resolve(Row row);
}
