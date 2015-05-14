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
	public void testDataStreamLines() {
		
		textFile.setStartLine(4);
		textFile.setEndLine(5);
		try {
			InputStream stream = textFile.getDataStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			assertEquals("foo", reader.readLine());
			assertEquals("bar", reader.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDataStreamInvertedStartEnd() {
		textFile.setStartLine(6);
		textFile.setEndLine(3);
		try {
			InputStream stream = textFile.getDataStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			assertTrue(reader.readLine() == null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Test
	public void testInvalidLineNumber() {
		textFile.setStartLine(0);
		textFile.setEndLine(100);
		try {
			InputStream stream = textFile.getDataStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			assertTrue(reader.readLine() == null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	@Test
	public void testNullEndLine() {
		textFile.setStartLine(6);
		try {
			InputStream stream = textFile.getDataStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			assertEquals("line 6",  reader.readLine());
			assertEquals("",  reader.readLine());
			assertEquals("footer",  reader.readLine());
			assertNull(reader.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Test
	public void testNoDataRange() {
		try {
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
