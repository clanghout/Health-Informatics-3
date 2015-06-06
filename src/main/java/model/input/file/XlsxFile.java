package model.input.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.data.DataTable;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class to represent a Microsoft excel (xlsx) ooxml file.
 * @author Paul
 *
 */
public class XlsxFile extends ExcelFile {

	private Logger logger = Logger.getLogger("XlsxFile");

	/**
	 * Creates a new XlsxFile.
	 * @param path The path to the xlsx file
	 * @throws FileNotFoundException When the file can not be found
	 */
	public XlsxFile(String path) throws FileNotFoundException {
		super(path);
	}

	@Override
	public DataTable createDataTable() throws IOException {

		FileInputStream file = new FileInputStream(getFile());
		try {
			Workbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			return createTable(rowIterator);
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading xlsx file", e);
			throw e;
		}
	}
}
