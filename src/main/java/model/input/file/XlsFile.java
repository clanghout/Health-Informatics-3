package model.input.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.data.DataTable;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Class to represent a Microsoft Excel (xls) file.
 * @author Paul
 *
 */
public class XlsFile extends ExcelFile {

	private Logger logger = Logger.getLogger("XlsFile");
	
	public XlsFile(String path) {
		super(path);
	}

	@Override
	public DataTable createDataTable() throws IOException {
		
		FileInputStream file = new FileInputStream(getFile());
		try {
			Workbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			return createTable(rowIterator);
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading xls file", e);
			throw e;
		}
	}

	@Override
	public String getFileTypeAsString() {
		return "xls";
	}
}
