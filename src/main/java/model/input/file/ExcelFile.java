package model.input.file;

import model.data.DataTable;
import model.data.value.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Class representing a general MS Excel file.
 * @author Paul
 *
 */
public abstract class ExcelFile extends DataFile {

	private static final String EXCEL_DATE = "exceldate";

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
				ColumnInfo column = getColumns().get(i);
				column.setName(headers.getCell(i).getStringCellValue());
				getBuilder().createColumn(column.getName(), column.getType());
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
				values[i] = toDataValue(cell, getColumns().get(i));
				if (values[i].isNull()) {
					nullCount++;
				}
			}
			if (nullCount == getColumns().size()) {
				break;
			}
			if (hasMetaData()) {
				values[values.length - 1] = getMetaDataValue();
			}
			getBuilder().createRow(values);
		}
	}

	private DataValue toDataValue(Cell cell, ColumnInfo columnInfo) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return DataValue.getNullInstance(columnInfo.getType());
			case Cell.CELL_TYPE_STRING:
				return parseStringValue(cell, columnInfo);
			case Cell.CELL_TYPE_NUMERIC:
				return parseNumValue(cell, columnInfo);
			case Cell.CELL_TYPE_BOOLEAN:
				return new BoolValue(cell.getBooleanCellValue());
			default:
				throw new UnsupportedOperationException(
						String.format("Cell type %s not supported", cell.getCellType()));
		}
	}

	private DataValue parseStringValue(Cell cell, ColumnInfo columnInfo) {
		if (cell.getStringCellValue().equals("NULL")) {
			return DataValue.getNullInstance(columnInfo.getType());
		} else if (isTemporalValue(columnInfo.getType())) {
			return parseTemporalValue(cell.getStringCellValue(), columnInfo);
		} else {
			return new StringValue(cell.getStringCellValue());
		}
	}

	private DataValue parseNumValue(Cell cell, ColumnInfo columnInfo) {
		if (isTemporalValue(columnInfo.getType())) {
			return parseExcelDateCell(cell, columnInfo);
		}
		
		double cellValue = cell.getNumericCellValue();
		
		if (columnInfo.getType() == IntValue.class) {
			return new IntValue((int) cellValue);
		} else if (columnInfo.getType() == FloatValue.class) {
			return new FloatValue((float) cellValue);
		} else {
			throw new UnsupportedOperationException(
				String.format("type %s not supported", columnInfo.getType()));
		}
	}

	private DataValue parseExcelDateCell(Cell cell, ColumnInfo columnInfo) {
		if (DateUtil.isCellDateFormatted(cell)) {
			Date date = cell.getDateCellValue();
			return parseDateCellValue(date, columnInfo.getType());
	
		} else {
			if (columnInfo.getFormat().equals(EXCEL_DATE)) {
				Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
				return parseDateCellValue(javaDate, columnInfo.getType());
			} else {
				return parseTemporalValue(
						String.valueOf(cell.getNumericCellValue()), columnInfo);
			}
		}
	}

	private DataValue parseDateCellValue(Date date, Class<? extends DataValue> type) {
		if (type == DateTimeValue.class) {
			Instant instant = Instant.ofEpochMilli(date.getTime());
			return new DateTimeValue(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));

		} else if (type == DateValue.class) {
			Instant instant = Instant.ofEpochMilli(date.getTime());
			return new DateValue(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));

		} else if (type == TimeValue.class) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return new TimeValue(calendar.get(Calendar.HOUR_OF_DAY),
								calendar.get(Calendar.MINUTE),
								calendar.get(Calendar.SECOND));
		}
		return null;
	}
}
