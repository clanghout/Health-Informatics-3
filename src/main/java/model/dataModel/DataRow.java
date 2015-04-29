package model.dataModel;
import Exceptions.*;
import java.util.HashMap;

/**
 * Created by jens on 4/29/15.
 */
public class DataRow {
    protected HashMap<DataColumn, DataValue> values;

    public DataRow() {
        values = new HashMap<DataColumn, DataValue>();
    }

    public DataRow(DataColumn[] c, DataValue[] v) throws ColumnValueMismatchException, ColumnValueTypeMismatchEception{
        if(c.length != v.length) {
            throw new ColumnValueMismatchException("Number of columns is not equal t the number of values");
        }
        for(int i = 0; i < c.length; i ++) {
            if(c[i].getType().getClass().isInstance(v[i])){
                values.put(c[i], v[i]);
            } else {
                throw new ColumnValueTypeMismatchEception("Type of value is not a subtype of column tpye");
            }
        }
    }

    public void setValue(DataColumn column, DataValue v) {
        values.put(column, v);
    }

    public DataValue getValue(DataColumn c) {
        return values.get(c);
    }

}
