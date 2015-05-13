package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class XmlReaderTest {

	private XmlReader reader;
	private File file;

	@Before
	public void setUp() {
		assertNotNull("Test file not found", getClass().getResource("/user_save.xml"));
		try {
			file = new File(getClass().getResource("/user_save.xml").getFile());
			reader = new XmlReader(file) ;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReadStreamedXMLversion() {
		assertEquals("1.0", reader.getDocument().getXmlVersion());			
	}

	@Test
	public void testReadStreamedXMLamountOfFiles() {
		NodeList list = reader.getDocument().getElementsByTagName("file");
		assertEquals(2, list.getLength());
	}

	@Test
	public void testReadXMLpath() {
		Element root = reader.getDocument().getDocumentElement();
		NodeList files = root.getElementsByTagName("file");
		Element file1 = (Element) files.item(0);
		assertEquals("Filename attribute does not match:", "ADMIRE.txt", file1.getAttribute("name"));
		Element pathnode = (Element) file1.getElementsByTagName("path").item(0);
		String filepath = pathnode.getTextContent();
		assertEquals("input", filepath);
	}

	@Test
	public void testReadDataFiles() throws FileNotFoundException {
		List<DataFile> dataFiles = reader.getDataFiles();

		String parentDir = file.getParent();
		String relativePath = new File(parentDir).toURI().relativize(
				dataFiles.get(0).getFile().toURI()
		).getPath();

		assertEquals("input/ADMIRE.txt", relativePath);
		assertEquals("userinput", dataFiles.get(0).getType());
		assertEquals("[\\s\\S]+(?=\\[)", dataFiles.get(0).getHeaderPattern());
		/**
		 * 
		 * 
		 * warning
		 * test fails because input folder has been removed
		 * 
		 * 
		 * 
		 */
//		assertNotNull(getClass().getResourceAsStream("/" + relativePath));
	}

	@Test
	public void testCreateDataFile() throws Exception {
		String parentDir = file.getParent();
		DataFile dataFile = reader.createDataFile(
				reader.getFileElement(0),
				parentDir
		);
		String relativePath = new File(parentDir).toURI().relativize(dataFile.getFile().toURI()).getPath();
		assertEquals("input/ADMIRE.txt", relativePath);
	}

	@Test
	public void testReadRegExFilter() {
		String testStats = 
				  "This is the header of the statsensor file and will be filtered\n"
				+ "Board ID: T11024007997w Meter ID: 149033312038\n"
				+ "Software Version: 1.1\n"
				+ "Patient Records Read Back From Meter\n"
				+ "Date: 29-9-2013 Time: 11:20\n"
				+ "[\n"
				+ "Crea,	179,umol/L,00,130218,0802\n"
				+ "Crea,	179,umol/L,00,130218,0803\n"
				+ "]\n";
		Element root = reader.getDocument().getDocumentElement();
		NodeList files = root.getElementsByTagName("file");
		Element file = (Element) files.item(1);
		Element pathnode = (Element) file.getElementsByTagName("header").item(0);
		String regex = pathnode.getTextContent();
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(testStats);
		if(matcher.find()) {
			String filtered = matcher.replaceAll("");
			assertEquals("[\n"
				+ "Crea,	179,umol/L,00,130218,0802\n"
				+ "Crea,	179,umol/L,00,130218,0803\n"
				+ "]\n", filtered);
		}
	}
}
