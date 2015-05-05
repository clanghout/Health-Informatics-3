package xml;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class XMLreaderTest {
	
	private XMLreader reader;
	
	@Before
	public void setUp() {
		assertNotNull("Test file not found", getClass().getResource("/user_save.xml"));
		try {
			reader = new XMLreader(getClass().getResourceAsStream("/user_save.xml")) ;
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
		assertEquals("/input", filepath);
	}
	
	@Test
	public void testReadDataFiles() {
		ArrayList<DataFile> dataFiles = reader.getDataFiles();
		assertEquals("/input/ADMIRE.txt", dataFiles.get(0).getPath());
		assertEquals("userinput", dataFiles.get(0).getType());
		assertTrue(getClass().getResourceAsStream(dataFiles.get(0).getPath()) != null);
	}
}
