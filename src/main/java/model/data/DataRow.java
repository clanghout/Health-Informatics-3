package model.data;

import exceptions.*;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Class that represents a row of data
 */
public class DataRow {
	private Logger log = Logger.getLogger("DataRow");

	protected HashMap<DataColumn, DataValue> values = new HashMap<DataColumn, DataValue>();

	/**
	 * create an empty row
	 */
	public DataRow() {

	}

	/**
	 * create a row and set for the columns c, the values v
	 *
	 * @param c columns that the row should have
	 * @param v calue of the columns
	 * @throws ColumnValueMismatchException    thrown when the number of columns is not equal to the number of values
	 * @throws ColumnValueTypeMismatchEception thrown when the value has a different type from what the columns expects
	 */
	public DataRow(DataColumn[] c, DataValue[] v) throws ColumnValueMismatchException, ColumnValueTypeMismatchEception {
		if (c.length != v.length) {
			ColumnValueMismatchException e = new ColumnValueMismatchException("Number of columns is not equal t the number of values");
			log.throwing(this.getClass().getSimpleName(), "constructor", e);
			throw e;
		}
		for (int i = 0; i < c.length; i++) {
			if (c[i].getType().isInstance(v[i])) {
				values.put(c[i], v[i]);
			} else {
				ColumnValueTypeMismatchEception e = new ColumnValueTypeMismatchEception("Type of value is not a subtype of column type");
				log.throwing(this.getClass().getSimpleName(), "constructor", e);
				throw e;
			}
		}
	}

	/**
	 * Add the column with the value to the row
	 *
	 * @param column thw column where the value belongs to
	 * @param v      the value of the added column
	 */
	public void setValue(DataColumn column, DataValue v) {
		values.put(column, v);
	}

	/**
	 * get the value of a column
	 *
	 * @param c the column where you want the value from
	 * @return
	 */
	public DataValue getValue(DataColumn c) {
		return values.get(c);
	}

}
