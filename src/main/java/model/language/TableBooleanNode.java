package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.BoolValue;

/**
 * Created by Boudewijn on 3-6-2015.
 */
class TableBooleanNode extends BooleanNode {

	private final ColumnIdentifier column;

	TableBooleanNode(ColumnIdentifier column) {
		super(null, null);

		this.column = column;
	}

	@Override
	DataDescriber<BoolValue> resolve(DataModel model) {
		return new TableValueDescriber<>(model, column);
	}
}
