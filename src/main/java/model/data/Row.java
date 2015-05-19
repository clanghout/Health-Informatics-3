package model.data;

import model.data.value.DataValue;

/**
 * Created by jens on 5/19/15.
 */
public interface Row {
	DataValue getValue(DataColumn column);
	void setValue(DataColumn column, DataValue value);
	void addConnection(DataConnection connection);
}
