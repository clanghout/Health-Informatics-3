package model.data.describer;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import model.data.Row;
import model.data.value.DataValue;
import model.language.ColumnIdentifier;

import java.util.Optional;

/**
 * This describes can be used to refer to a table.column even if that table doesn't exist
 * at the moment of creation.
 *
 * Created by Boudewijn on 3-6-2015.
 * @param <T> The type of DataValue this describer is referring to.
 */
public final class TableValueDescriber<T extends DataValue> extends DataDescriber<T> {

	private DataModel model;
	private ColumnIdentifier column;

	public TableValueDescriber(DataModel model, ColumnIdentifier column) {
		this.model = model;
		this.column = column;
	}

	@Override
	public T resolve(Row row) {
		Optional<DataTable> tableOptional = model.getByName(column.getTable());

		if (tableOptional.isPresent()) {
			DataTable table = tableOptional.get();
			DataColumn dataColumn = table.getColumn(column.getColumn());

			return (T) row.getValue(dataColumn);
		} else {
			throw new IllegalArgumentException(
					String.format("Table %s couldn't be found.", column.getTable())
			);
		}

	}
}
