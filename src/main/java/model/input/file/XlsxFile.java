package model.input.file;

import java.io.FileInputStream;
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
	
	public XlsxFile(String path) {
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

	@Override
	public String getFileTypeAsString() {
		return "xlsx";
	}
}
