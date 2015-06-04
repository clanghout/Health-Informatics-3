package model.data.value;

import model.input.file.DataFile;

/**
 * @author Paul
 */
public class FileValue extends DataValue {

    private DataFile file;

    public FileValue(DataFile file) {
        this.file = file;
    }

    @Override
    public Object getValue() {
        return this.file;
    }

    @Override
    public String toString() {
        return file.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FileValue && ((FileValue) obj).getValue().equals(this.getValue());
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public DataValue copy() {
        return new FileValue(this.file);
    }
}
