package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.NumberValue;

/**
 * Created by Boudewijn on 27-5-2015.
 */
class TableNumberNode extends NumberNode {

	private final ColumnIdentifier column;

	TableNumberNode(ColumnIdentifier column) {
		super(null, null);

		this.column = column;
	}

	DataDescriber<NumberValue> resolve(DataModel model) {
		return new RowValueDescriber<>(
				model.getByName(
						column.getTable()
				).getColumn(column.getColumn()));
	}
}
