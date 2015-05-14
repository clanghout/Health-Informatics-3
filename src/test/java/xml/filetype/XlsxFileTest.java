package xml.filetype;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

public class XlsxFileTest {

	XlsxFile xlsxFile;
	
	@Before
	public void setUp() {
		String file = getClass().getResource("/input/xlsxfile.xlsx").getFile();
		xlsxFile = new XlsxFile(file);
	}
	
	@Test
	public void testDataStream() {
		try {
			InputStream stream = xlsxFile.getDataStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			assertEquals("foo\tbar", reader.readLine());
			assertEquals("row2\tbar\tbar\tbat", reader.readLine());
			assertEquals("row3\tbar\tbar\tbats", reader.readLine());
			assertNull(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
