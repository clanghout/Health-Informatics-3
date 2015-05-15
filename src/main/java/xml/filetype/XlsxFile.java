package xml.filetype;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class to represent a Microsoft excel (xlsx) ooxml file.
 * @author Paul
 *
 */
public class XlsxFile extends ExcelFile {

	public XlsxFile(String path) {
		super(path);
	}

	@Override
	public InputStream getDataStream() throws FileNotFoundException {

		FileInputStream file = new FileInputStream(getFile());
		try {
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			return createStream(rowIterator);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
}
