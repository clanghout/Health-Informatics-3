package model.data;

import model.data.value.DataValue;

/**
 * Created by jens on 5/19/15.
 */
public interface Row {
	DataValue getValue(String column);

}
