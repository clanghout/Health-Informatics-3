package xml.filetype;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

public class XlsFileTest {

	XlsFile xlsFile;
	
	@Before
	public void setUp() {
		String file = getClass().getResource("/input/xlsfile.xls").getFile();
		xlsFile = new XlsFile(file);
	}
	
	@Test
	public void testDataStream() {
		try {
			InputStream stream = xlsFile.getDataStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			assertEquals("foo\tbar\t", reader.readLine());
			assertEquals("row2\tbar\tbar\tbat\t", reader.readLine());
			assertEquals("row3\tbar\tbar\tbats\t", reader.readLine());
			assertNull(reader.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
