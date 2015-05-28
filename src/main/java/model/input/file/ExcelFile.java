package model.input.file;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
	public InputStream createStream(Iterator<Row> rowIterator) throws FileNotFoundException {

		StringBuilder builder = new StringBuilder();
		
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.iterator();
			
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				builder.append(cell.getStringCellValue() + "\t");
			}
			
			builder.append("\n");
		}
		InputStream newStream = new ByteArrayInputStream(
		        	builder.toString().getBytes(StandardCharsets.UTF_8)
		);
		return newStream;
	}

}
