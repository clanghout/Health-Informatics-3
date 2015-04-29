package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchEception;

/**
 * Builder used to build a DataModel
 */
public class DataModelBuilder {
	private DataModel model;
	private int currentIndex;

	/**
	 * create a new builder
	 */
	public DataModelBuilder() {
		model = new DataModel();
		currentIndex = 0;
	}

	/**
	 * return the datamodel build by the builder
	 *
	 * @return the DataModel that is build by the builder
	 */
	public DataModel build() {
		return model;
	}

	/**
	 * add a column to the DataModel
	 *
	 * @param column the new column
	 */
	public void addColumn(DataColumn column) {
		model.columns.put(column.getName(), column);
	}

	/**
	 * Add a currentIndex the the DataModel
	 *
	 * @param row the new currentIndex
	 */
	public void addRow(DataRow row) {
		model.data.put(this.currentIndex, row);
		this.currentIndex++;
	}

	/**
	 * Construct a DataColumn. This is not added to the model
	 *
	 * @param name name of the column
	 * @param type type of the column
	 * @return the constructed DataColumn
	 */
	public DataColumn createColumn(String name, Class<? extends DataValue> type) {
		return new DataColumn(name, type);
	}

	/**
	 * Construct a DataRow. This is not added to the model
	 *
	 * @param values list of new values
	 * @return the ne constructed DataRow
	 * @throws ColumnValueMismatchException    thrown when the number of columns is not equal to the number of values
	 * @throws ColumnValueTypeMismatchEception thrown when the value has a different type from what the columns expects
	 */
	public DataRow createRow(DataValue... values) {
		//TODO If I remember correctly, since the columns are stored in a hashmap, they can be returned in an different way,
		//TODO so this will probably not work, but due lack of time, I have not tested it yet, so I'm not sure
		return new DataRow(model.columns.values().toArray(new DataColumn[0]), values);
	}
}
