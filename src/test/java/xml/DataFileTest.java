package xml;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class DataFileTest {

	@Test
	public void testFilterHeader() {
		DataFile df = new DataFile("/input/statsensor.txt", "statsensor", "[\\s\\S]+(?=\\[)");
		try {
			InputStream st = df.filterHeader();
			BufferedReader reader = new BufferedReader(new InputStreamReader(st));
			assertEquals("[", reader.readLine());
			assertEquals("Crea,	179,umol/L,00,130218,0802", reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


}
