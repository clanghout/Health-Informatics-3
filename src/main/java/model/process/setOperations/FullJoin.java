package model.process.setOperations;

import model.data.*;
import model.data.value.DataValue;
import model.language.Identifier;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jens on 6/10/15.
 */
public class FullJoin extends Join {
	private Identifier<DataTable> leftId;
	private Identifier<DataTable> rightId;
	private DataTable left;
	private DataTable right;
	private boolean fullLeft;
	private boolean fullRight;

	private FullJoin(String name, boolean fullLeft, boolean fullRight) {
		super(name);
		this.fullLeft = fullLeft;
		this.fullRight = fullRight;
	}
	/**
	 * Join two tables.
	 * @param left left table
	 * @param right right table
	 * @param fullLeft true if all left rows should exist in the result
	 * @param fullRight true if all right rows should exist in the result
	 */
	public FullJoin(String name, Identifier<DataTable> left,
					Identifier<DataTable> right,
					boolean fullLeft,
					boolean fullRight) {
		this(name, fullLeft, fullRight);
		this.leftId = left;
		this.rightId = right;
	}

	/**
	 * Join two tables.
	 * @param left left table
	 * @param right right table
	 * @param fullLeft true if all left rows should exist in the result
	 * @param fullRight true if all right rows should exist in the result
	 */
	public FullJoin(String name, DataTable left,
					DataTable right,
					boolean fullLeft,
					boolean fullRight) {
		this(name, fullLeft, fullRight);
		this.left = left;
		this.right = right;
		this.fullLeft = fullLeft;
		this.fullRight = fullRight;
	}


	@Override
	protected void joinTable() {
		processFullJoin(getLeft(), getRight());
	}

	@Override
	protected Table getTable() {
		return new CombinedDataTable(getLeft(), getRight());
	}

	/**
	 * Get the left table.
	 * @return the left table.
	 */
	private DataTable getLeft() {
		if (left == null) {
			left =  getDataModel().getByName(leftId.getName()).get();
		}
		return left;
	}

	/**
	 * Get the right table.
	 * @return the right table.
	 */
	private DataTable getRight() {
		if (right == null) {
			right =  getDataModel().getByName(rightId.getName()).get();
		}
		return right;
	}

	/**
	 * Create the rows for the join.
	 * @param leftRow row in the left table
	 * @param rightRow row in the right table
	 */
	private void createRow(DataRow leftRow, DataRow rightRow) {
		DataRow newRow = new DataRow();
		for (Map.Entry<DataColumn, DataColumn> entry : getMappingColumns().entrySet()) {
			if (leftRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), leftRow.getValue(entry.getKey()));
			} else {
				checkValue(newRow, entry.getValue(),
						DataValue.getNullInstance(entry.getValue().getType()));
			}
			if (rightRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), rightRow.getValue(entry.getKey()));
			}
		}
		newRow.addCodes(leftRow.getCodes());
		newRow.addCodes(rightRow.getCodes());
		getBuilder().addRow(newRow);
	}

	/**
	 * Perform a Left/right/both full join, kind of join is based in fullLeft and fullRight boolean.
	 */
	private void processFullJoin(DataTable left, DataTable right) {
		Set<Row> rightAdd = new HashSet<>();
		for (DataRow leftRow : (Iterable<DataRow>) left) {
			boolean leftAdd = false;

			for (DataRow rightRow : (Iterable<DataRow>) right) {
				CombinedDataRow comb = new CombinedDataRow();
				comb.addDataRow(leftRow);
				comb.addDataRow(rightRow);
				if (getConstraint().resolve(comb).getValue()) {
					createRow(leftRow, rightRow);
					leftAdd = true;
					rightAdd.add(rightRow);
				}
			}
			if (!leftAdd && fullLeft) {
				createRow(leftRow, new DataRow());
			}
			if (fullRight) {
				addNotAddedRighRows(rightAdd);
			}
		}

	}

	/**
	 * Add the rows from the right table that are not added to the result.
	 * @param rightAdd set of rows that are set in the result table.
	 */
	private void addNotAddedRighRows(Set<Row> rightAdd) {
		for (DataRow rightRow : (Iterable<DataRow>) right) {
			if (!rightAdd.contains(right)) {
				createRow(new DataRow(), rightRow);
			}
		}
	}

}
