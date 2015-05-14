package xml.filetype;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import exceptions.DataFileNotRecognizedException;
import xml.filetype.DataFile;
import xml.filetype.PlainTextFile;

public class DataFileTest {

	@Test
	public void testCreateDataFile() {
		String file = getClass().getResource("/input/statsensor.txt").getFile();

		DataFile df = DataFile.createDataFile(file, "plaintext");
		assertTrue(df instanceof PlainTextFile);
		
		df.setStartLine(7);
		df.setEndLine(8);
		InputStream st;
		try {
			st = df.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(st));
		assertEquals("Crea,	179,umol/L,00,130218,0802", reader.readLine());
		assertEquals("Crea,	179,umol/L,00,130218,0803", reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test(expected = DataFileNotRecognizedException.class)
	public void testCreateInvalidDataFile() {
		DataFile.createDataFile("/input/statsensor.txt", "invalidType");
	}
	
	@Test
	public void testCreateXlsFile(){
		DataFile df = DataFile.createDataFile("/input/xlsfile.xls", "xls");
		assertTrue(df instanceof XlsFile);
	}

	@Test
	public void testCreateXlsxFile(){
		DataFile df = DataFile.createDataFile("/input/xlsxfile.xlsx", "xlsx");
		assertTrue(df instanceof XlsxFile);
	}
}
