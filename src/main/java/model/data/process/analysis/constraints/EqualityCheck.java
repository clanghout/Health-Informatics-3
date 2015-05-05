package model.data.process.analysis.constraints;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataValue;

/**
 * A check for equality.
 *
 * Created by Boudewijn on 5-5-2015.
 */
public class EqualityCheck extends Constraint {

    private final DataColumn column;
    private final DataValue value;

    public EqualityCheck(DataColumn column, DataValue value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean check(DataRow row) {
        return row.getValue(column).equals(value);
    }
}
