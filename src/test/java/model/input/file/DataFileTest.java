package model.input.file;

import static org.junit.Assert.*;

import java.io.*;

import model.data.DataTable;
import org.junit.Test;

import model.exceptions.DataFileNotRecognizedException;

/**
 * JUnit test for the DataFile class.
 * @author Paul
 *
 */
public class DataFileTest {

	@Test
	public void testCreateDataFile() throws Exception {
		String file = getClass().getResource("/model/input/statsensor.txt").getFile();

		DataFile dataFile = DataFile.createDataFile(file, "plaintext");
		assertTrue(dataFile instanceof PlainTextFile);
		
		dataFile.setStartLine(7);
		dataFile.setEndLine(8);

		DataTable dataTable = dataFile.createDataTable();

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
