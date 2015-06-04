package model.language;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.StringValue;

/**
 * Created by Boudewijn on 3-6-2015.
 */
class TableStringNode extends StringNode {

	private final ColumnIdentifier column;

	TableStringNode(ColumnIdentifier column) {
		super(null, null);
		this.column = column;
	}

	@Override
	DataDescriber<StringValue> resolve(DataModel model) {
		return new TableValueDescriber<>(model, column);
	}
}
