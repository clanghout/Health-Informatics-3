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

	private Document document;
	private  ArrayList<DataFile> dataFiles;
	
	/**
	 * Creates a new XMLreader.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @param stream the stream that will be read.
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
	 * @param file the file that will be read.
	 */
	public XMLreader(File file)
			throws ParserConfigurationException, SAXException, IOException {
		read(file);
	}
	
	/**
	 * Reads the xml file and returns a Document that can be used 
	 * to extract data from the xml file.
	 * 
	 * @param file the xml file
	 * @return the read document
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
	 * @param stream the inputstream
	 * @return the read document
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
		NodeList filesList = root.getElementsByTagName("file");
		
		for (int i = 0; i < filesList.getLength(); i++) {
			Element elem = (Element) filesList.item(i);
			String fileName = elem.getAttribute("name");
			String type = elem.getElementsByTagName("type").item(0).getTextContent();
			String path = elem.getElementsByTagName("path").item(0).getTextContent();
			DataFile dataFile = new DataFile(path + File.separator + fileName, type);
			dataFiles.add(dataFile);
		}
		
		return document;
	}
	
	/**
	 * Returns the buffered read document.
	 * @return the buffered read document
	 */
	public Document getDocument() {
		return this.document;
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
