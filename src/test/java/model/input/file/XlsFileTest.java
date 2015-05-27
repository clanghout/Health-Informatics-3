package model.input.file;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the XlsFile class.
 * @author Paul
 *
 */
public class XlsFileTest {

	private XlsFile xlsFile;
	
	@Before
	public void setUp() {
		String file = getClass().getResource("/model/input/xlsfile.xls").getFile();
		xlsFile = new XlsFile(file);
	}
	
	@Test
	public void testDataStream() throws IOException {
		InputStream stream = xlsFile.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		assertEquals("foo\tbar\t", reader.readLine());
		assertEquals("row2\tbar\tbar\tbat\t", reader.readLine());
		assertEquals("row3\tbar\tbar\tbats\t", reader.readLine());
		assertNull(reader.readLine());
		reader.close();
	}
}
