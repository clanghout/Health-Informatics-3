package model.output;

import model.data.value.*;
import model.input.file.ColumnInfo;
import model.input.file.DataFile;
import model.input.file.PlainTextFile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Paul.
 */
public class XmlWriterTest {

	XmlWriter writer;
	@Mock
	PlainTextFile textFile;

	@Before
	public void setUp() throws Exception {
		textFile = mock(PlainTextFile.class);
	}

	@Test
	public void testCreateDocument() throws Exception {

		when(textFile.getFile()).thenReturn(new File("/path/to/Prettyname.txt"));
		when(textFile.hasFirstRowAsHeader()).thenReturn(false);
		List<ColumnInfo> cols = new ArrayList<>();
		
		cols.add(new ColumnInfo("someInts", IntValue.class));
		cols.add(new ColumnInfo("someStrings", StringValue.class));
		cols.add(new ColumnInfo("someDates", DateValue.class));
		cols.add(new ColumnInfo("someFloats", FloatValue.class));
		cols.add(new ColumnInfo("someTimes", TimeValue.class));
		cols.add(new ColumnInfo("someDateTimes", DateTimeValue.class));

		when(textFile.getColumns()).thenReturn(cols);
		ArrayList<DataFile> dataFiles = new ArrayList<>();
		dataFiles.add(textFile);

		XmlWriter writer = new XmlWriter(dataFiles);
		Document doc = writer.createDocument();

		assertEquals("1.0", doc.getXmlVersion());
		Element root = doc.getDocumentElement();
		assertEquals("input", root.getTagName());
		assertEquals(1, root.getChildNodes().getLength());
		assertEquals("file", root.getFirstChild().getNodeName());
		Element fileElem = (Element) root.getElementsByTagName("file").item(0);
		assertEquals("Prettyname.txt", fileElem.getAttribute("name"));

		Element columns = (Element) fileElem
				.getElementsByTagName("columns").item(0);

		assertEquals("columns", columns.getTagName());
		assertEquals(6, columns.getChildNodes().getLength());
		assertEquals("false", columns.getAttribute("firstrowheader"));

		assertEquals("int", ((Element)columns.getElementsByTagName("column")
				.item(0)).getAttribute("type"));
		assertEquals("string", ((Element)columns.getElementsByTagName("column")
				.item(1)).getAttribute("type"));
		assertEquals("date", ((Element)columns.getElementsByTagName("column")
				.item(2)).getAttribute("type"));
		assertEquals("float", ((Element)columns.getElementsByTagName("column")
				.item(3)).getAttribute("type"));
		assertEquals("time", ((Element)columns.getElementsByTagName("column")
				.item(4)).getAttribute("type"));
		assertEquals("datetime", ((Element)columns.getElementsByTagName("column")
				.item(5)).getAttribute("type"));

	}

	@Test
	public void testWrite() throws Exception {

	}
}