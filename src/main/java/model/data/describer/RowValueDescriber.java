package model.data.describer;

import model.data.DataColumn;
import model.data.Row;
import model.data.value.DataValue;

/**
 * A describer for values contained within a table.
 *
 * <b>Don't use this describer in any DataProcess. It depends on a DataColumn which may not
 * exist yet when this class is constructed. The parser doesn't support this class. Use
 * TableValueDescriber instead.</b>
 * Created by Boudewijn on 11-5-2015.
 * @param <Type> The type of data you want to describe.
 */
public final class RowValueDescriber<Type extends DataValue> extends DataDescriber<Type> {

	private DataColumn column;

	/**
	 * Construct a new RowValueDescriber.
	 * @param column The column containing the value you want to describe
	 */
	public RowValueDescriber(DataColumn column) {
		this.column = column;
	}

	/**
	 * Resolves the value described by this describer.
	 *
	 * Note: No ClassCastException will be thrown when the type doesn't match. This is due to the
	 * Java generics type erasure.
	 * @param row The row the value should be resolved from
	 * @return The value described
	 */
	@Override
	public Type resolve(Row row) {
		return (Type) row.getValue(column);
	}
}
