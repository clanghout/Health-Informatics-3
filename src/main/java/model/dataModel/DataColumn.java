package model.dataModel;

/**
 * Created by jens on 4/29/15.
 */
public class DataColumn {
    DataValue type;
    String name;

    public DataColumn(DataValue t, String n) {
        type = t;
        name = n;
    }

    public String getName() {
        return name;
    }

    public DataValue getType() {
        return type;
    }
}
