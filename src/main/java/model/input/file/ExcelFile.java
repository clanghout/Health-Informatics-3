package model.input.file;

import java.io.FileNotFoundException;
import java.util.Iterator;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

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
		if(hasFirstRowAsHeader()) {
			Row headers = rowIterator.next();
			for(int i = 0; i < getColumnTypes().length; i++) {
				getColumns().put(headers.getCell(i).getStringCellValue(), getColumnTypes()[i]);
			}
		} else {			
			for (String key : getColumns().keySet()) {
				builder.createColumn(key, getColumns().get(key));
			}
		}
		
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			DataValue[] values = new DataValue[getColumns().size()];
			for (int i = 0; i < getColumns().size(); i++) {
				DataValue value = null;
				Cell cell = row.getCell(i, Row.RETURN_NULL_AND_BLANK);
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING: {
						value = new StringValue(cell.getStringCellValue());
						break;
					}
					case Cell.CELL_TYPE_NUMERIC: {
						double cellValue = cell.getNumericCellValue();
						value = ((cellValue % 1) == 0) ? 
								new FloatValue((float) cellValue) : new IntValue((int) cellValue);
						break;					
					}
					case Cell.CELL_TYPE_BLANK: {
						value = null;
						break;
					}
					default: throw new UnsupportedOperationException(
							String.format("Cell type %s not supported", cell.getCellType())
					);
				}
				values[i] = value;
			}
			builder.createRow(values);
		}
		return builder.build();
	}
}
