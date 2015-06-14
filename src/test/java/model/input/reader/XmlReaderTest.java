package model.input.reader;

import model.input.file.DataFile;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Junit test for the XmlReader class.
 * @author Paul
 *
 */
public class XmlReaderTest {

	private XmlReader reader;
	private File file;

	@Before
	public void setUp() {
		assertNotNull("Test file not found", getClass().getResource("/user_save.xml"));
		file = new File(getClass().getResource("/user_save.xml").getFile());
		reader = new XmlReader();
	}

	@Test
	public void testReadStreamedXMLversion() throws ParserConfigurationException, 
												SAXException, IOException {
		assertEquals("1.0", reader.read(file).getXmlVersion());
	}

	@Test
	public void testReadStreamedXMLamountOfFiles() throws ParserConfigurationException, 
													SAXException, IOException {
		NodeList list;
		list = reader.read(file).getElementsByTagName("file");
		assertEquals(3, list.getLength());
	}
	
	@Test(expected = NullPointerException.class)
	public void testReadXMLNullDocument() throws ParserConfigurationException, 
												SAXException, IOException {
		Document list = reader.getDocument();
		list.getElementsByTagName("file");
	}

	@Test
	public void testReadXMLpath() throws ParserConfigurationException, SAXException, IOException {
		Element root;
		root = reader.read(file).getDocumentElement();
		NodeList files = root.getElementsByTagName("file");
		Element file1 = (Element) files.item(1);
		assertEquals("Filename attribute does not match:", "ADMIRE.txt", 
				file1.getAttribute("name"));
		Element pathnode = (Element) file1.getElementsByTagName("path").item(0);
		String filepath = pathnode.getTextContent();
		assertEquals("model/input", filepath);
	}
	
	@Test
	public void testReadDataFiles() throws Exception {
		reader.read(file);
		List<DataFile> dataFiles = reader.getDataFiles();
		String path = dataFiles.get(0).getPath();

		String parentDir = file.getParent();

		String relativePath = new File(parentDir).toURI().relativize(
				new File(path).toURI()).getPath();
		assertEquals("model/input/ADMIRE2.txt", relativePath);
		assertNotNull(getClass().getResourceAsStream("/" + relativePath));
	}

	@Test
	public void testCreateDataFileWithPath() throws Exception {
		String parentDir = file.getParent();
		reader.read(file);
		DataFile dataFile = reader.createDataFile(
				reader.getFileElement(1)
		);
		String relativePath = new File(parentDir).toURI().relativize(
				dataFile.getFile().toURI()).getPath();
		assertEquals("model/input/ADMIRE.txt", relativePath);
	}
}
