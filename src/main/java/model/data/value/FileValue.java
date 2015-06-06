package model.data.value;

import model.input.file.DataFile;

/**
 * Class to represent a file as metadata.
 *
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
		return file.getPath();
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
