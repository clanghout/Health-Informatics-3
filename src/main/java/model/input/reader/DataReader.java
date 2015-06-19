package model.input.reader;

import model.data.DataModel;
import model.data.DataTable;
import model.input.file.DataFile;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class can be used to construct a datamodel from xml.
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReader {

	private Logger log = Logger.getLogger("DataReader");

	private XmlReader xmlReader;

	/**
	 * Creates a new DataReader to read datafiles specified in an xml file.
	 * @param reader The xml reader to use
	 */
	public DataReader(XmlReader reader) {
		this.xmlReader = reader;
	}

	/**
	 * Returns the xml reader.
	 * @return The xml reader.
	 */
	public XmlReader getXmlReader() {
		return xmlReader;
	}

	/**
	 * Reads the datafiles using an xmlreader.
	 * @throws IOException When the file can not be read
	 * @throws SAXException Thrown by the sax parser
	 * @throws ParserConfigurationException when the xml can not be parsed
	 */
	public void read(File file) throws IOException, SAXException, ParserConfigurationException {
		try {
			xmlReader = new XmlReader();
			xmlReader.read(file);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			log.log(Level.SEVERE, "XML file could not be read", e);
			throw e;
		}
	}

	/**
	 * Sets up a new DataModel that contains the read rows.
	 * @return A new DataModel
	 * @throws IOException When the file is missing or corrupted
	 */
	public DataModel createDataModel() throws IOException {
		DataModel model = new DataModel();
		List<DataFile> dataFiles = xmlReader.getDataFiles();
		for (DataFile dataFile : dataFiles) {
			DataTable table = dataFile.createDataTable();
			
			model.add(table);
		}
		return model;
	}
}
