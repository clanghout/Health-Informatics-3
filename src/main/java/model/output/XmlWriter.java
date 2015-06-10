package model.output;

import model.data.value.DataValue;
import model.input.file.DataFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for writing an xml save file created by the user.
 * DataFiles are added to the list and can then be written to the file
 *
 * @author Paul.
 */
public class XmlWriter {

	private List<DataFile> dataFiles;
	private Logger logger = Logger.getLogger("XmlWriter");
	private Document document;

	/**
	 * Creates a new XmlWriter.
	 */
	public XmlWriter(List<DataFile> dataFiles) {
		logger.log(Level.INFO, "Create new XMLWriter");
		this.dataFiles = new ArrayList<>(dataFiles);
	}

	/**
	 * Creates the xml document structure that will be written.
	 * @return The document
	 * @throws ParserConfigurationException Thrown by DocumentBuilderFactory
	 * @throws FileNotFoundException When a dataFile is not found
	 * @throws ClassNotFoundException When a classtype can not be found
	 */
	public Document createDocument()
			throws ParserConfigurationException, FileNotFoundException, ClassNotFoundException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		this.document = document;

		document.setXmlVersion("1.0");
		Element root = document.createElement("input");
		document.appendChild(root);

		for (DataFile file : this.dataFiles) {
			root.appendChild(createFileElement(file));
		}

		document.normalize();
		return document;
	}

	/**
	 * Writes the xml to a file.
	 */
	public void write(File file) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(createDocument());

			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);

		} catch (ParserConfigurationException
				| FileNotFoundException
				| ClassNotFoundException
				| TransformerException e) {
			e.printStackTrace();
		}
	}

	private Element createFileElement(DataFile dataFile)
			throws FileNotFoundException, ClassNotFoundException {
		Element res = document.createElement("file");
		res.setAttribute("name", dataFile.getFile().getPath());
		res.appendChild(createFileTypeElement(dataFile));
		res.appendChild(createBoundsElement(dataFile));
		res.appendChild(createColumnsElement(dataFile));

		return res;
	}

	private Element createFileTypeElement(DataFile dataFile) {
		Element type = document.createElement("type");
		type.setTextContent(dataFile.getFileTypeAsString());

		return type;
	}

	private Element createBoundsElement(DataFile dataFile) {
		Element res = document.createElement("data");
		Element startline = document.createElement("start");
		Element endline = document.createElement("end");

		res.appendChild(startline);
		res.appendChild(endline);

		startline.setTextContent(String.valueOf(dataFile.getStartLine()));
		endline.setTextContent(String.valueOf(dataFile.getEndLine()));

		return res;
	}

	private Element createColumnsElement(DataFile dataFile)
			throws ClassNotFoundException {
		Element res = document.createElement("columns");

		if (dataFile.hasFirstRowAsHeader()) {
			res.setAttribute("firstrowheader", "true");
		} else {
			res.setAttribute("firstrowheader", "false");
		}

		Map<String, Class<? extends DataValue>> columns = dataFile.getColumns();
		for (Map.Entry<String, Class<? extends DataValue>> entry : columns.entrySet()) {
			res.appendChild(createColumnElements(entry.getKey(), entry.getValue()));
		}

		return res;
	}

	private Element createColumnElements(String columnName, Class type)
			throws ClassNotFoundException {
		Element res = document.createElement("column");
		res.setAttribute("type", DataFile.getStringColumnType(type));
		res.setTextContent(columnName);

		return res;
	}
}
