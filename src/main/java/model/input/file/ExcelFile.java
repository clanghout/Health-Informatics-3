package model.input.file;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.*;
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

	public ExcelFile(String path) {
		super(path);
	}
	
	/**
	 * Creates an InputStream formed by an Iterator that iterates over the rows of either an
	 * xls file or an xlsx file. Returns a stream with rows as lines in which the columns are
	 * delimited with tab characters.
	 * @param rowIterator The Iterator over the rows
	 * @return the new stream of rows
	 * @throws FileNotFoundException when the file is not found
	 */
	protected DataTable createTable(Iterator<Row> rowIterator) throws FileNotFoundException {

		DataTableBuilder builder = new DataTableBuilder();
		builder.setName(this.getFile().getName().replace(".", ""));
		if (hasFirstRowAsHeader()) {
			Row headers = rowIterator.next();
			for (int i = 0; i < getColumnTypes().length; i++) {
				getColumns().put(headers.getCell(i).getStringCellValue(), getColumnTypes()[i]);
				builder.createColumn(
						headers.getCell(i).getStringCellValue(), getColumnTypes()[i]);
			}
		} else {
			for (String key : getColumns().keySet()) {
				builder.createColumn(key, getColumns().get(key));
			}
		}

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			DataValue[] values = new DataValue[getColumns().size()];
			int nullCount = 0;
			for (int i = 0; i < getColumns().size(); i++) {
				Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
				values[i] = toDataValue(cell);
				if (values[i] instanceof NullValue) {
					nullCount++;
				}
			}
			if (nullCount == getColumns().size()) { break; }
			builder.createRow(values);
		}
		return builder.build();
	}

	private DataValue toDataValue(Cell cell) {
		DataValue value;
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = new StringValue(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC: {
				value = determineNumValue(cell);
				break;
			}
			case Cell.CELL_TYPE_BLANK:
				value = new NullValue();
 				break;
			default:
				throw new UnsupportedOperationException(
						String.format("Cell type %s not supported", cell.getCellType()));
		}
		return value;
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
