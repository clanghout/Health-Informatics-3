package model.input.reader;

import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DataValue;
import model.data.value.StringValue;
import model.input.file.DataFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class can be used to read the data from the StatSenser
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReader {

	private Logger log = Logger.getLogger("DataReader");

	private XmlReader xmlReader;
	
	public DataReader(File filename) throws Exception {
		try {
			xmlReader = new XmlReader();
			xmlReader.read(filename);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			log.log(Level.SEVERE, "XML file could not be read", e);
			throw e;
		}
	}

	public DataModel createDataModel() throws IOException {
		DataModel model = new DataModel();
		List<DataFile> dataFiles = xmlReader.getDataFiles();
		for(DataFile dataFile : dataFiles) {
			DataTable table = dataFile.createDataTable();
			
			model.add(table);
		}
		return model;
	}
}
