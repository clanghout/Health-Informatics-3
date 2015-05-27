package model.input.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

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
	public InputStream getDataStream() throws IOException {
		
		FileInputStream file = new FileInputStream(getFile());
		try {
			workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			return createStream(rowIterator);
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading xls file", e);
			throw e;
		}
	}
}
