package model.input.file;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.value.*;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the XlsFile class.
 * @author Paul
 *
 */
public class ExcelFileTest {

	private XlsxFile xlsxFile;
	private XlsFile xlsFile;

	@Before
	public void setUp() throws Exception {
		String file = getClass().getResource("/model/input/xlsx1.xlsx").getFile();
		xlsxFile = new XlsxFile(file);

		String file2 = getClass().getResource("/model/input/xls1.xls").getFile();
		xlsFile = new XlsFile(file2);

		xlsxFile.setColumns(createmapping(), createList());
		xlsFile.setColumns(createmapping(), createList());

	}

	private LinkedHashMap<String, Class<? extends DataValue>> createmapping() {
		LinkedHashMap<String, Class<? extends DataValue>> mapping = new LinkedHashMap<>();mapping.put("string", StringValue.class);
		mapping.put("float", FloatValue.class);
		mapping.put("int", IntValue.class);
		return mapping;
	}

	private List<Class<? extends DataValue>> createList() {
		List<Class<? extends DataValue>> list = new ArrayList<>();
		list.add(StringValue.class);
		list.add(FloatValue.class);
		list.add(IntValue.class);
		return list;
	}

	@Test
	public void testReadXlsx() throws Exception {
		DataTable table = xlsxFile.createDataTable();
		List<DataColumn> columns = table.getColumns();

		assertEquals(StringValue.class, columns.get(0).getType());
		assertEquals(FloatValue.class, columns.get(1).getType());
		assertEquals(IntValue.class, columns.get(2).getType());

		DataRow row = table.getRow(0);

		assertEquals(new StringValue("string"), row.getValue(table.getColumn("string")));
		assertEquals(new FloatValue(0.666f), row.getValue(table.getColumn("float")));
		assertEquals(new IntValue(666), row.getValue(table.getColumn("int")));
	}

	@Test
	public void testReadXls() throws Exception {
		DataTable table = xlsFile.createDataTable();
		List<DataColumn> columns = table.getColumns();

		assertEquals(StringValue.class, columns.get(0).getType());
		assertEquals(FloatValue.class, columns.get(1).getType());
		assertEquals(IntValue.class, columns.get(2).getType());

		DataRow row = table.getRow(0);

		assertEquals(new StringValue("string"), row.getValue(table.getColumn("string")));
		assertEquals(new FloatValue(0.666f), row.getValue(table.getColumn("float")));
		assertEquals(new IntValue(666), row.getValue(table.getColumn("int")));
	}

}
