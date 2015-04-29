package model.dataModel;

import java.util.HashMap;
import java.util.Iterator;

/**
 * class that represents the data that should be analyzed
 */
public class DataModel {
    protected HashMap<Integer, DataRow> data;
    protected HashMap<String, DataColumn> columns;

    /**
     * create a new empty DataModel
     */
    public DataModel() {
        data = new HashMap<Integer, DataRow>();
    }

    /**
     * Get a specific row
     * @param i number of the row
     * @return the requested row
     */
    public DataRow getRow(int i) {
        return data.get(i);
    }

    /**
     * return an iterator of the rows
     * @return an iterator that contains al the rows
     */
    public Iterator<DataRow> getRows() {
        return data.values().iterator();
    }

    /**
     * get the columns of the datamodel
     * @return a hashmap that contains all the columns
     */
    public HashMap<String, DataColumn> getColumns() {
        return columns;
    }

}
