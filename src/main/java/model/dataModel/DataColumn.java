package model.dataModel;

/**
 * Class that specified what the data in a column is
 */
public class DataColumn {
    DataValue type;
    String name;

    /**
     * Create a column
     * @param t type of the column
     * @param n name of the column
     */
    public DataColumn(DataValue t, String n) {
        type = t;
        name = n;
    }

    /**
     * get the name of the column
     * @return the name of the column
     */
    public String getName() {
        return name;
    }

    /**
     * get the type of the column
     * @return the type of the column
     */
    public DataValue getType() {
        return type;
    }
}
