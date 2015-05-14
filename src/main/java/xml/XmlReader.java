package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xml.filetype.DataFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for reading an xml file that was saved by the user.
 * @author Paul
 *
 */
public class XmlReader {

	private static final String FILE_TAG        = "file";
	private static final String TYPE_TAG        = "type";
	private static final String PATH_TAG        = "path";
	private static final String NAME_ATTRIBUTE  = "name";
	private static final String DATA_TAG        = "data";
	private static final String START_TAG       = "start";
	private static final String END_TAG         = "end";
	
	
	
	private Document document;
	private NodeList filesList;
	private List<DataFile> dataFiles;

	/**
	 * Creates a new XmlReader.
	 * @param file The file that will be read.
	 */
	public XmlReader() {}
	
	/**
	 * Reads the xml file and returns a Document that can be used 
	 * to extract data from the xml file.
	 * 
	 * @param file The xml file
	 * @return The read document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Document read(File file)
			throws ParserConfigurationException, SAXException, IOException {
		try (FileInputStream stream = new FileInputStream(file)) {
			return read(stream, file.getParent());
		}
	}
	
	/**
	 * Reads the xml from an InputStream and returns a Document that can be used
	 * to extract data from the xml file.
	 * @param stream The InputStream
	 * @return The read document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Document read(InputStream stream, String parentDir)
			throws ParserConfigurationException, SAXException, IOException {
		
		dataFiles = new ArrayList<DataFile>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		document = builder.parse(stream);
		document.normalize();
		Element root = (Element) document.getDocumentElement();
		filesList = root.getElementsByTagName(FILE_TAG);
		
		for (int i = 0; i < filesList.getLength(); i++) {
			Element elem = getFileElement(i);
			dataFiles.add(createDataFile(elem, parentDir));
		}
		
		return document;
	}
	
	/**
	 * Creates a DataFile from a file Element.
	 * @return a new DataFile
	 * @param elem The file element read from the xml file
	 * @param parentDir The parent of the file
	 */
	public DataFile createDataFile(Element elem, String parentDir) {
		String fileName  = elem.getAttribute(NAME_ATTRIBUTE);
		String type      = elem.getElementsByTagName(TYPE_TAG).item(0).getTextContent();
		Element data = (Element) elem.getElementsByTagName(DATA_TAG).item(0);
		int start = Integer.parseInt(data.getElementsByTagName(START_TAG).item(0).getTextContent());
		int end   = Integer.parseInt(data.getElementsByTagName(END_TAG).item(0).getTextContent());
		String completePath;
		if(elem.getElementsByTagName(PATH_TAG).item(0) != null) {
			String path     = elem.getElementsByTagName(PATH_TAG).item(0).getTextContent();
			completePath = parentDir + File.separator + path + File.separator + fileName;
		}
		else {
			completePath = fileName;
		}
		DataFile theDataFile = DataFile.createDataFile(completePath, type);
		theDataFile.setStartLine(start);
		theDataFile.setEndLine(end);
		return theDataFile;
	}
	
	/**
	 * Returns the buffered read document.
	 * @return the buffered read document
	 */
	public Document getDocument() {
		return this.document;
	}
	
	/**
	 * Returns the i-th file Element of the xml file.
	 * @param i The index of the file Element
	 * @return The Element
	 */
	public Element getFileElement(int i) {
		return (Element) filesList.item(i);
	}
	
	/**
	 * Returns an List of all the datafiles
	 * that were read from the xml file.
	 * @return An unmodifiable list of the data files
	 */
	public List<DataFile> getDataFiles() {
		return Collections.unmodifiableList(dataFiles);
	}
}
