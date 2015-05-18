package xml.filetype;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the PlainTextFile class.
 * @author Paul
 *
 */
public class PlainTextFileTest {

	PlainTextFile textFile;
	
	@Before
	public void setUp() {
		String file = getClass().getResource("/input/plaintext.txt").getFile();
		textFile = new PlainTextFile(file);
	}
	
	@Test
	public void testDataStreamLines() throws IOException {
		
		textFile.setStartLine(4);
		textFile.setEndLine(5);
		InputStream stream = textFile.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		assertEquals("foo", reader.readLine());
		assertEquals("bar", reader.readLine());
		reader.close();
	}
	
	@Test
	public void testDataStreamInvertedStartEnd() throws IOException {
		textFile.setStartLine(6);
		textFile.setEndLine(3);
		InputStream stream = textFile.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		assertTrue(reader.readLine() == null);
		reader.close();
	}

	@Test
	public void testInvalidLineNumber() throws IOException {
		textFile.setStartLine(0);
		textFile.setEndLine(100);
		InputStream stream = textFile.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		assertTrue(reader.readLine() == null);
		reader.close();
	}
	
	@Test
	public void testNullEndLine() throws IOException {
		textFile.setStartLine(6);
		InputStream stream = textFile.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		assertEquals("line 6",  reader.readLine());
		assertEquals("",  reader.readLine());
		assertEquals("footer",  reader.readLine());
		assertNull(reader.readLine());
		reader.close();
	}

	@Test
	public void testNoDataRange() throws IOException {
		InputStream stream = textFile.getDataStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		assertEquals("This is plain text line one",  reader.readLine());
		assertEquals("",  reader.readLine());
		assertEquals("line 3",  reader.readLine());
		assertEquals("foo",  reader.readLine());
		assertEquals("bar",  reader.readLine());
		assertEquals("line 6",  reader.readLine());
		assertEquals("",  reader.readLine());
		assertEquals("footer",  reader.readLine());
		assertNull(reader.readLine());
		reader.close();
	}
}
