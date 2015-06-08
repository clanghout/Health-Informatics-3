package model.input.file;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.value.*;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the PlainTextFile class.
 * @author Paul
 *
 */
public class PlainTextFileTest {

	private PlainTextFile textFile;
	
	@Before
	public void setUp() throws Exception {
		String file = getClass().getResource("/model/input/plaintext.txt").getFile();
		textFile = new PlainTextFile(file);
		textFile.setDelimiter(" ");
		LinkedHashMap<String, Class<? extends DataValue>> mapping = new LinkedHashMap<>();
		mapping.put("test", StringValue.class);
		mapping.put("int", IntValue.class);
		mapping.put("float", FloatValue.class);
		mapping.put("string", StringValue.class);
		List<Class<? extends DataValue>> list = new ArrayList<>();
		list.add(StringValue.class);
		list.add(IntValue.class);
		list.add(FloatValue.class);
		list.add(StringValue.class);
		textFile.setColumns(mapping, list);
		textFile.createMetaDataValue("meta", "string");
	}

	@Test
	public void test() throws Exception {
		DataTable table = textFile.createDataTable();
		List<DataColumn> columns = table.getColumns();

		assertEquals(StringValue.class, columns.get(0).getType());
		assertEquals(IntValue.class, columns.get(1).getType());
		assertEquals(FloatValue.class, columns.get(2).getType());
		assertEquals(StringValue.class, columns.get(3).getType());

		DataRow row = table.getRow(0);

		assertEquals(new StringValue("test"), row.getValue(table.getColumn("test")));
		assertEquals(new IntValue(5), row.getValue(table.getColumn("int")));
		assertEquals(new FloatValue(3.5f), row.getValue(table.getColumn("float")));
		assertEquals(new StringValue("dingen"), row.getValue(table.getColumn("string")));
		assertEquals(new StringValue("plaintext"),
				row.getValue(table.getColumn("meta")));
	}
}
