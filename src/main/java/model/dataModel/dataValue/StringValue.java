package model.dataModel.dataValue;

import model.dataModel.DataValue;

/**
 * Data Class containing a value with type String
 */
public class StringValue extends DataValue{
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringValue<" + value + ">";
    }
}
