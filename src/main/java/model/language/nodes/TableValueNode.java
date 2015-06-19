package model.language.nodes;

import model.data.DataModel;
import model.process.describer.DataDescriber;
import model.process.describer.TableValueDescriber;
import model.data.value.DataValue;
import model.language.ColumnIdentifier;

/**
 * Represents a value in a table.
 *
 * Created by Boudewijn on 4-6-2015.
 * @param <T> The type of DataValue this node is describing.
 */
public class TableValueNode<T extends DataValue> extends ValueNode<T> {

	private final ColumnIdentifier columnIdentifier;

	/**
	 * Construct a new TableValueNode.
	 * @param columnIdentifier The identifier for the column and table.
	 */
	public TableValueNode(ColumnIdentifier columnIdentifier) {
		super(null, null);
		this.columnIdentifier = columnIdentifier;
	}

	/**
	 * Resolve the value in this TableValueNode.
	 * @param model The model to be used.
	 * @return A DataDescriber for the value.
	 */
	@Override
	public DataDescriber<T> resolve(DataModel model) {
		return new TableValueDescriber<>(model, columnIdentifier);
	}
}
