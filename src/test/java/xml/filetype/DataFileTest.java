package xml.filetype;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import xml.filetype.DataFile;

public class DataFileTest {

	@Test
	public void testFilterHeader() throws Exception {
		String file = getClass().getResource("/input/statsensor.txt").getFile();

		DataFile df = DataFile.createDataFile(file, "plaintext");

		InputStream st = df.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(st));
		assertEquals("[", reader.readLine());
		assertEquals("Crea,	179,umol/L,00,130218,0802", reader.readLine());
	}


}
