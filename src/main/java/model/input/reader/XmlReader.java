package model.input.reader;

import model.data.value.DataValue;
import model.input.file.DataFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Class for reading an xml file that was saved by the user.
 * @author Paul
 *
 */
public class XmlReader {

	/**
	 * The name of the filetag in the xml file.
	 */
	private static final String FILE_TAG = "file";

	/**
	 * The name of the typetag in the xml file.
	 */
	private static final String TYPE_TAG = "type";

	/**
	 * The name of the pathtag in the xml file.
	 */
	private static final String PATH_TAG = "path";

	/**
	 * The name of the filename attribute in the xml file.
	 */
	private static final String NAME_ATTRIBUTE = "name";

	/**
	 * The name of the datatag in the xml file.
	 */
	private static final String DATA_TAG = "data";

	/**
	 * The name of the starttag in the xml file.
	 */
	private static final String START_TAG = "start";

	/**
	 * The name of the endtag in the xml file.
	 */
	private static final String END_TAG = "end";
	
	/**
	 * The name of the column tag in the xml file.
	 */
	private static final String COLUMN_TAG = "column";
	
	/**
	 * The name of the columns tag in the xml file.
	 */
	private static final String COLUMNS_TAG = "columns";
	
	/**
	 * The name of the firstrowheader tag in the xml file.
	 */
	private static final String FIRST_ROW_HEADER_ATTRIBUTE = "firstrowheader";

	/**
	 * The name of the metadata tag in the xml file.
	 */
	private static final String METADATA_TAG = "metadata";

	private Logger log = Logger.getLogger("XmlReader");

	private Document document;
	private NodeList filesList;
	private List<DataFile> dataFiles;

	/**
	 * Creates a new XmlReader.
	 */
	public XmlReader() { }

	/**
	 * Reads the xml file and returns a Document that can be used
	 * to extract data from the xml file.
	 * @param file The xml file
	 * @return The read document
	 * @throws ParserConfigurationException Thrown by the DocumentBuilder
	 * @throws IOException If an IO error occurs
	 * @throws SAXException Thrown by the SAX parser if the document can not be parsed
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
	 * @throws ParserConfigurationException Thrown by the DocumentBuilder
	 * @throws IOException If an IO error occurs
	 * @throws SAXException Thrown by the SAX parser if the document can not be parsed
	 */
	public Document read(InputStream stream, String parentDir)
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
			dataFiles.add(createDataFile(elem, parentDir));
		}
		
		return document;
	}
	
	/**
	 * Creates a DataFile from a file Element.
	 * @return a new DataFile
	 * @param elem The file element read from the xml file
	 * @param parentDir The parent of the file
	 * @throws FileNotFoundException When the file can not be found
	 */
	public DataFile createDataFile(Element elem, String parentDir) throws FileNotFoundException {
		String type = elem.getElementsByTagName(TYPE_TAG).item(0).getTextContent();
		String completePath = createPath(elem, parentDir);
		DataFile theDataFile = DataFile.createDataFile(completePath, type);

		Element columnsElement = (Element) elem.getElementsByTagName(COLUMNS_TAG).item(0);
		Element data = (Element) elem.getElementsByTagName(DATA_TAG).item(0);
		theDataFile = setStartEndLine(data, theDataFile);
		String firstRowHeader = columnsElement.getAttribute(FIRST_ROW_HEADER_ATTRIBUTE);
		if (firstRowHeader != null && firstRowHeader.equals("true")) {
			theDataFile.setFirstRowAsHeader(true);
		}
		
		NodeList columns = columnsElement.getElementsByTagName(COLUMN_TAG);
		setColumnTypes(theDataFile, columns);
		NodeList metaData = elem.getElementsByTagName(METADATA_TAG);
		theDataFile = setMetaData(theDataFile, metaData);

		return theDataFile;
	}

	private DataFile setMetaData(DataFile theDataFile, NodeList metaData) {
		Element metaDataElem = ((Element) metaData.item(0));

		if ((metaDataElem != null
				&& metaDataElem.getAttribute("type") != null)
				&& metaDataElem.getAttribute("name") != null) {
			String type = metaDataElem.getAttribute("type");
			String name = metaDataElem.getAttribute("name");
			theDataFile.createMetaDataValue(name, type);
			theDataFile.setHasMetaData(true);
		}
		return theDataFile;
	}

	private DataFile setColumnTypes(DataFile theDataFile, NodeList columns) {
		if (theDataFile.hasFirstRowAsHeader()) {
			theDataFile.addColumnTypes(createTypesArray(columns));
		} else {
			theDataFile = setColumn(columns, theDataFile);
		}
		return theDataFile;
	}
	
	private String createPath(Element elem, String parentDir) {
		String fileName  = elem.getAttribute(NAME_ATTRIBUTE);
		Element pathElement = (Element) elem.getElementsByTagName(PATH_TAG).item(0);
		String completePath;
		if (pathElement != null) {
			String path  = elem.getElementsByTagName(PATH_TAG).item(0).getTextContent();
			completePath = parentDir + File.separator + path + File.separator + fileName;
		} else {
			completePath = fileName;
		}
		return completePath;
	}

	private DataFile setColumn(NodeList columns, DataFile dataFile) {
		for (int i = 0; i < columns.getLength(); i++) {
			Element columnElement = (Element) columns.item(i);
			String typeAttribute = columnElement.getAttribute("type");
			Class columnType = DataFile.getColumnType(typeAttribute);
			dataFile.addColumn(columnElement.getTextContent(), columnType);
		}
		return dataFile;
	}
		
	
	private Class[] createTypesArray(NodeList columns) {
		Class[] types = new Class[columns.getLength()];
		for (int i = 0; i < columns.getLength(); i++) {
			Element columnElement = (Element) columns.item(i);
			String typeAttribute = columnElement.getAttribute("type");
			types[i] = DataFile.getColumnType(typeAttribute);
		}
		return types;
	}
	
	/**
	 * Decorates the constructed DataFile with the start and end line.
	 * @param dataElement The data element of the file read from xml
	 * @param dataFile The constructed DataFile
	 * @return the decorated DataFile
	 */
	private DataFile setStartEndLine(Element dataElement, DataFile dataFile) {
		if (dataElement != null) {
			Element startElement = (Element) dataElement.getElementsByTagName(START_TAG).item(0);
			if (startElement != null) {
				dataFile.setStartLine(Integer.parseInt(startElement.getTextContent()));
			}
			Element endElement = (Element) dataElement.getElementsByTagName(END_TAG).item(0);
			if (endElement != null) {
				dataFile.setEndLine(Integer.parseInt(endElement.getTextContent()));
			}
		}
		return dataFile;
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
