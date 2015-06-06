package model.input.file;

import model.data.DataTable;
import model.data.value.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileNotFoundException;
import java.util.Iterator;

/**
 * Class representing a general MS Excel file.
 * @author Paul
 *
 */
public abstract class ExcelFile extends DataFile {

	/**
	 * Creates a new ExcelFile.
	 * @param path The path to the Excel file
	 * @throws FileNotFoundException when the file can not be found
	 */
	public ExcelFile(String path) throws FileNotFoundException {
		super(path);
	}

	/**
	 * Creates a new DataTable from the excel file.
	 * @param rowIterator The Iterator over the rows
	 * @return A new DataTable
	 * @throws FileNotFoundException when the file is not found
	 */
	protected DataTable createTable(Iterator<Row> rowIterator) throws FileNotFoundException {

		if (hasFirstRowAsHeader()) {
			Row headers = rowIterator.next();
			for (int i = 0; i < getColumnTypes().length; i++) {
				getColumns().put(headers.getCell(i).getStringCellValue(), getColumnTypes()[i]);
			}
		} else {			
			for (String key : getColumns().keySet()) {
				getBuilder().createColumn(key, getColumns().get(key));
			}
		}
		addMetaDataFileColumn();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			DataValue[] values = new DataValue[getColumns().size() + 1];
			for (int i = 0; i < getColumns().size(); i++) {
				Cell cell = row.getCell(i, Row.RETURN_NULL_AND_BLANK);
				values[i] = toDataValue(cell);
			}
			values[values.length - 1] = new FileValue(this);
			getBuilder().createRow(values);
		}
		return getBuilder().build();
	}

	private DataValue toDataValue(Cell cell) {
		DataValue value = null;
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = new StringValue(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				double cellValue = cell.getNumericCellValue();
				value = (cellValue % 1 == 0)
						? new IntValue((int) cellValue) : new FloatValue((float) cellValue);
				break;
			case Cell.CELL_TYPE_BLANK:
				value = new StringValue("");
				break;
			default: throw new UnsupportedOperationException(
					String.format("Cell type %s not supported", cell.getCellType())
			);
		}
		return value;
	}
}
