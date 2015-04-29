package model.dataModel;

import Exceptions.ColumnValueMismatchException;
import Exceptions.ColumnValueTypeMismatchEception;

/**
 * Builder used to build a DataModel
 */
public class DataModelBuilder {
    private DataModel model;
    private int row;

    /**
     * create a new builder
     */
    public DataModelBuilder() {
        model = new DataModel();
        row = 0;
    }

    /**
     * return the datamodel build by the builder
     * @return the DataModel that is build by the builder
     */
    public DataModel build() {
        return model;
    }

    /**
     * add a column to the DataModel
     * @param c the new column
     */
    public void addColumn(DataColumn c) {
        model.columns.put(c.getName(), c);
    }

    /**
     * Add a row the the DataModel
     * @param r the new row
     */
    public void addRow(DataRow r) {
        model.data.put(row, r);
        row++;
    }

    /**
     * Construct a DataColumn. This is not added to the model
     * @param name name of the column
     * @param v type of the column
     * @return the constructed DataColumn
     */
    public DataColumn createColumn(String name, DataValue v) {
        return new DataColumn(v, name);
    }

    /**
     * Construct a DataRow. This is not added to the model
     * @param v list of new values
     * @return the ne constructed DataRow
     * @throws ColumnValueMismatchException thrown when the number of columns is not equal to the number of values
     * @throws ColumnValueTypeMismatchEception thrown when the value has a different type from what the columns expects
     */
    public DataRow createRow(DataValue[] v) throws ColumnValueMismatchException, ColumnValueTypeMismatchEception {
       //TODO If I remember correctly, since the columns are stored in a hashmap, they can be returned in an different way,
        //TODO so this will probably not work, but due lack of time, I have not tested it yet, so I'm not sure
        return new DataRow(model.columns.values().toArray(new DataColumn[0]), v);
    }
}
