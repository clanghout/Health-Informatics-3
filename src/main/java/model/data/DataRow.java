package model.data;

import exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class that represents a row of data
 */
public class DataRow {
	private Logger log = Logger.getLogger("DataRow");

	private Map<String, DataValue> values = new HashMap<String, DataValue>();

	/**
	 * create an empty row
	 */
	public DataRow() {

	}

	/**
	 * create a row and set for the columns c, the values v
	 *
	 * @param columnArray columns that the row should have
	 * @param valueArray value of the columns
	 * @throws ColumnValueMismatchException    thrown when the number of columns is not equal to the number of values
	 * @throws ColumnValueTypeMismatchException thrown when the value has a different type from what the columns expects
	 */
	public DataRow(DataColumn[] columnArray, DataValue[] valueArray) throws ColumnValueMismatchException, ColumnValueTypeMismatchException {
		if (columnArray.length != valueArray.length) {
			ColumnValueMismatchException e = new ColumnValueMismatchException("Number of columns is not equal t the number of values");
			log.throwing(this.getClass().getSimpleName(), "constructor", e);
			throw e;
		}
		for (int i = 0; i < columnArray.length; i++) {
			if (columnArray[i].getType().isInstance(valueArray[i])) {
				values.put(columnArray[i].getName(), valueArray[i]);
			} else {
				ColumnValueTypeMismatchException e = new ColumnValueTypeMismatchException("Type of value is not a subtype of column type");
				log.throwing(this.getClass().getSimpleName(), "constructor", e);
				throw e;
			}
		}
	}

	/**
	 * Add the column with the value to the row
	 *
	 * @param column thw column where the value belongs to
	 * @param value the value of the added column
	 */
	public void setValue(DataColumn column, DataValue value) {values.put(column.getName(), value);}
	public void setValue(String column, DataValue value) {
		values.put(column, value);
	}


	/**
	 * get the value of a column
	 *
	 * @param column the column where you want the value from
	 * @return the value of the column of this row
	 */
	public DataValue getValue(DataColumn column) {
		return values.get(column.getName());
	}
	public DataValue getValue(String column) {
		return values.get(column);
	}

//	public ArrayList<String> getRowValues() {
//		Iterator<Map.Entry<String, DataValue>> iterator = values.entrySet().iterator();
//		ArrayList<String> res = new ArrayList<String>();
//		while(iterator.hasNext()){
//			res.add(iterator.next().getValue().toString());
//		}
//		return res;
//	}

}
