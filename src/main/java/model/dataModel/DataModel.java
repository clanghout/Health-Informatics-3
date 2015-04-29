package model.dataModel;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jens on 4/29/15.
 */
public class DataModel {
    protected HashMap<Integer, DataRow> data;
    protected HashMap<String, DataColumn> columns;

    public DataModel() {
        data = new HashMap<Integer, DataRow>();
    }

    public DataRow getRow(int i) {
        return data.get(i);
    }

    public Iterator<DataRow> getRows() {
        return data.values().iterator();
    }

    public HashMap<String, DataColumn> getColumns() {
        return columns;
    }

}
