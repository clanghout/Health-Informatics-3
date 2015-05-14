package xml;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import xml.filetype.DataFile;

public class DataFileTest {

	@Test
	public void testCreateDataFile() throws Exception {
		String file = getClass().getResource("/input/statsensor.txt").getFile();

		DataFile df = DataFile.createDataFile(file, "plaintext");
		df.setBeginLine(7);
		df.setEndLine(8);
		InputStream st = df.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(st));
		assertEquals("Crea,	179,umol/L,00,130218,0802", reader.readLine());
		assertEquals("Crea,	179,umol/L,00,130218,0803", reader.readLine());
	}
}
