package xml.filetype;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class to represent a Microsoft excel (xlsx) ooxml file.
 * @author Paul
 *
 */
public class XlsxFile extends DataFile {

	public XlsxFile(String path) {
		super(path);
	}

	@Override
	public InputStream getDataStream() throws FileNotFoundException {

		FileInputStream file = new FileInputStream(getFile());
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			String res = "";
			while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                 
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    res += cell.getStringCellValue() + "\t";
                }
                res += "\n";
	        }
	        InputStream newStream = new ByteArrayInputStream(
	        	res.getBytes(StandardCharsets.UTF_8)
			);
	        
	        return newStream;

		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
}
