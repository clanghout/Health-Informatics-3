package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class for reading an xml file that was saved by the user.
 * @author Paul
 *
 */
public class XMLreader {

	private static final String FILE_TAG		= "file";
	private static final String HEADER_TAG		= "header";
	private static final String TYPE_TAG		= "type";
	private static final String PATH_TAG		= "path";
	private static final String NAME_ATTRIBUTE	= "name";
	
	private Document document;
	private NodeList filesList;
	private ArrayList<DataFile> dataFiles;
	/**
	 * Creates a new XMLreader.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @param Stream the stream that will be read.
	 */
	public XMLreader(InputStream stream)
			throws ParserConfigurationException, SAXException, IOException {
		read(stream);
	}

	/**
	 * Creates a new XMLreader.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @param File the file that will be read.
	 */
	public XMLreader(File file)
			throws ParserConfigurationException, SAXException, IOException {
		read(file);
	}
	
	/**
	 * Reads the xml file and returns a Document that can be used 
	 * to extract data from the xml file.
	 * 
	 * @param File the xml file
	 * @return The read document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Document read(File file)
			throws ParserConfigurationException, SAXException, IOException {
		FileInputStream stream = new FileInputStream(file);
		return read(stream);
	}
	
	/**
	 * Reads the xml from an inputstream and returns a Document that can be used
	 * to extract data from the xml file.
	 * @param Stream the inputstream
	 * @return The read document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Document read(InputStream stream)
			throws ParserConfigurationException, SAXException, IOException {
		
		dataFiles = new ArrayList<DataFile>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		document = builder.parse(stream);
		document.normalize();
		Element root = document.getDocumentElement();
		filesList = root.getElementsByTagName(FILE_TAG);
		
		for (int i = 0; i < filesList.getLength(); i++) {
			Element elem = getFileElement(i);
			dataFiles.add(createDataFile(elem));
		}
		
		return document;
	}
	
	/**
	 * Creates a DataFile from a file Element.
	 * @return a new DataFile
	 */
	public DataFile createDataFile(Element elem) {
		String fileName	= elem.getAttribute(NAME_ATTRIBUTE);
		String type		= elem.getElementsByTagName(TYPE_TAG).item(0).getTextContent();
		String path		= elem.getElementsByTagName(PATH_TAG).item(0).getTextContent();
		String header	= elem.getElementsByTagName(HEADER_TAG).item(0).getTextContent();
		
		return new DataFile(path + File.separator + fileName, type, header);
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
		Element elem = (Element) filesList.item(i);
		return elem;
	}
	
	/**
	 * Returns an ArrayList of all the datafiles 
	 * that were read from the xml file.
	 * @return
	 */
	public ArrayList<DataFile> getDataFiles() {
		return dataFiles;
	}
}
