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

		xlsFile.addColumn("string", StringValue.class);
		xlsFile.addColumn("float", FloatValue.class);
		xlsFile.addColumn("int", IntValue.class);

		xlsxFile.addColumn("string", StringValue.class);
		xlsxFile.addColumn("float", FloatValue.class);
		xlsxFile.addColumn("int", IntValue.class);
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
