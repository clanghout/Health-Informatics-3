package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Class for reading an xml file that was saved by the user.
 * @author Paul
 *
 */
public class XMLreader {
	
	/*
	 * The document that is read from the xml file.
	 */
	protected Document document;
	
	/**
	 * Creates a new XMLreader.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public XMLreader(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		read(stream);
	}

	/**
	 * Creates a new XMLreader.
	 * @param file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XMLreader(File file) throws ParserConfigurationException, SAXException, IOException {
		read(file);
	}
	
	/**
	 * Reads the xml file and returns a Document that can be used to extract data from the xml file.
	 * @param file the xml file
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Document read(File file) throws ParserConfigurationException, SAXException, IOException {
		FileInputStream stream = new FileInputStream(file);
		return read(stream);
	}
	
	/**
	 * Reads the xml from an inputstream and returns a Document that can be used to extract data from the xml file.
	 * @param stream the inputstream
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Document read(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		document = builder.parse(stream);
		document.normalize();
		return document;
	}
	
	public Document getDocument() {
		return this.document;
	}

}
