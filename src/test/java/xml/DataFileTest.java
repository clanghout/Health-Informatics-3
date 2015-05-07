package xml;

import static org.junit.Assert.*;

import java.io.*;
import java.net.URLClassLoader;

import org.junit.Test;

public class DataFileTest {

	@Test
	public void testFilterHeader() throws Exception {
		String file = getClass().getResource("/input/statsensor.txt").getFile();

		DataFile df = new DataFile(
				file,
				"statsensor",
				"[\\s\\S]+(?=\\[)"
		);

		InputStream st = df.filterHeader();
		BufferedReader reader = new BufferedReader(new InputStreamReader(st));
		assertEquals("[", reader.readLine());
		assertEquals("Crea,	179,umol/L,00,130218,0802", reader.readLine());
	}


}
