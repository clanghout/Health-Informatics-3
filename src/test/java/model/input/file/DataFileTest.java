package model.input.file;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import model.exceptions.DataFileNotRecognizedException;

/**
 * JUnit test for the DataFile class.
 * @author Paul
 *
 */
public class DataFileTest {

	@Test
	public void testCreateDataFile() throws IOException {
		String file = getClass().getResource("/model/input/statsensor.txt").getFile();

		DataFile df = DataFile.createDataFile(file, "plaintext");
		assertTrue(df instanceof PlainTextFile);
		
		df.setStartLine(7);
		df.setEndLine(8);
		InputStream st;
		st = df.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(st));
		assertEquals("Crea,	179,umol/L,00,130218,0802", reader.readLine());
		assertEquals("Crea,	179,umol/L,00,130218,0803", reader.readLine());
		reader.close();
	}
	
	@Test(expected = DataFileNotRecognizedException.class)
	public void testCreateInvalidDataFile() {
		DataFile.createDataFile("/model/input/statsensor.txt", "invalidType");
	}
	
	@Test
	public void testCreateXlsFile() {
		DataFile df = DataFile.createDataFile("/model/input/xlsfile.xls", "xls");
		assertTrue(df instanceof XlsFile);
	}

	@Test
	public void testCreateXlsxFile() {
		DataFile df = DataFile.createDataFile("/model/input/xlsxfile.xlsx", "xlsx");
		assertTrue(df instanceof XlsxFile);
	}
}
