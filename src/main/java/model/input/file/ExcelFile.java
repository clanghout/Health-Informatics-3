package model.input.file;

import model.data.DataTable;
import model.data.value.*;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
	 */
	public ExcelFile(String path) {
		super(path);
	}

	/**
	 * Creates a new DataTable from the excel file.
	 * @param rowIterator The Iterator over the rows
	 * @return A new DataTable
	 * @throws FileNotFoundException when the file is not found
	 */
	protected DataTable createTable(Iterator<Row> rowIterator) throws FileNotFoundException {

		getBuilder().setName(getFile().getName().replace(".", ""));
		if (hasFirstRowAsHeader()) {
			Row headers = rowIterator.next();
			for (int i = 0; i < getColumns().size(); i++) {
				getColumns().get(i).setName(headers.getCell(i).getStringCellValue());
			}
		} else {
			for (ColumnInfo column : getColumns()) {
				getBuilder().createColumn(column.getName(), column.getType());
			}
		}
		if (hasMetaData()) {
			getBuilder().createColumn(getMetaDataColumnName(), getMetaDataType());
		}
		addRows(rowIterator);
		return getBuilder().build();
	}

	private void addRows(Iterator<Row> rowIterator) {
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			DataValue[] values;
			if (hasMetaData()) {
				values = new DataValue[getColumns().size() + 1];
			} else {
				values = new DataValue[getColumns().size()];
			}
			int nullCount = 0;
			for (int i = 0; i < getColumns().size(); i++) {
				Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
				values[i] = toDataValue(cell, getColumns().get(i).getType());
				if (values[i].isNull()) {
					nullCount++;
				}
			}
			if (nullCount == getColumns().size()) { break; }
			if (hasMetaData()) {
				values[values.length - 1] = getMetaDataValue();
			}
			getBuilder().createRow(values);
		}
	}

	private DataValue toDataValue(Cell cell, Class<? extends DataValue> type) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return new StringValue(cell.getStringCellValue());
			case Cell.CELL_TYPE_NUMERIC: {
				return determineNumValue(cell);
			}
			case Cell.CELL_TYPE_BLANK:
				return createNullValue(type);
			default:
				throw new UnsupportedOperationException(
						String.format("Cell type %s not supported", cell.getCellType()));
		}
	}

	private DataValue createNullValue(Class<? extends DataValue> type) {
		if (type == StringValue.class) {
			return new StringValue(null);
		} else
		if (type == BoolValue.class) {
			return new BoolValue(null);
		} else
		if (type == IntValue.class) {
			return new IntValue(null);
		} else
		if (type == FloatValue.class) {
			return new FloatValue(null);
		} else
		if (type == DateTimeValue.class) {
			return new DateTimeValue(null);
		}
		throw new UnsupportedOperationException(
				String.format("type %s not supported", type));
	}

	private DataValue determineNumValue(Cell cell) {
		DataValue value;
		if (DateUtil.isCellDateFormatted(cell)) {
			GregorianCalendar calendar = (GregorianCalendar) DateUtil.getJavaCalendar(
					cell.getNumericCellValue(), true);

			value = new DateTimeValue(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					calendar.get(Calendar.SECOND));
		} else {
			double cellValue = cell.getNumericCellValue();
			value = (cellValue % 1 == 0)
					? new IntValue((int) cellValue) : new FloatValue((float) cellValue);
		}
		return value;
	}
}
