package model.dataModel;

import Exceptions.ColumnValueMismatchException;
import Exceptions.ColumnValueTypeMismatchEception;

/**
 * Created by jens on 4/29/15.
 */
public class DataModelBuilder {
    private DataModel model;
    private int row;

    public DataModelBuilder() {
        model = new DataModel();
        row = 0;
    }

    public DataModel build() {
        return model;
    }

    public void addColumn(DataColumn c) {
        model.columns.put(c.getName(), c);
    }

    public void addRow(DataRow r) {
        model.data.put(row, r);
        row++;
    }

    public DataColumn createColumn(String name, DataValue v) {
        return new DataColumn(v, name);
    }

//    public DataRow createRow(DataValue[] v) throws ColumnValueMismatchException, ColumnValueTypeMismatchEception {
//        return new DataRow(model.columns.entrySet().toArray(), v);
//    }
}
