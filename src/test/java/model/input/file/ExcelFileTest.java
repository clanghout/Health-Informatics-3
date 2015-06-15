package model.input.file;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.value.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test for the XlsFile class.
 * @author Paul
 *
 */
public class ExcelFileTest {

	private XlsxFile xlsxFile;
	private XlsxFile xlsxFile2;
	private XlsFile xlsFile;
	private XlsFile xlsFile2;

	@Before
	public void setUp() throws Exception {
		String file = getClass().getResource("/model/input/xlsx1.xlsx").getFile();
		xlsxFile = new XlsxFile(file);

		String file2 = getClass().getResource("/model/input/xls1.xls").getFile();
		xlsFile = new XlsFile(file2);

		String file3 = getClass().getResource("/model/input/xlsx2.xlsx").getFile();
		xlsxFile2 = new XlsxFile(file3);

		String file4 = getClass().getResource("/model/input/xls2.xls").getFile();
		xlsFile2 = new XlsFile(file4);

		// Create the specifications of the files
		xlsFile.addColumnInfo(new ColumnInfo("string", StringValue.class));
		xlsFile.addColumnInfo(new ColumnInfo("float", FloatValue.class));
		xlsFile.addColumnInfo(new ColumnInfo("int", IntValue.class));

		xlsxFile.addColumnInfo(new ColumnInfo("string", StringValue.class));
		xlsxFile.addColumnInfo(new ColumnInfo("float", FloatValue.class));
		xlsxFile.addColumnInfo(new ColumnInfo("int", IntValue.class));

		xlsxFile2.setFirstRowAsHeader(true);
		xlsxFile2.createMetaDataValue("M374D474", "string");
		xlsxFile2.addColumnInfo(new ColumnInfo(IntValue.class));
		xlsxFile2.addColumnInfo(new ColumnInfo(StringValue.class));
		xlsxFile2.addColumnInfo(new ColumnInfo(DateTimeValue.class));
		xlsxFile2.addColumnInfo(new ColumnInfo(FloatValue.class));
		xlsxFile2.addColumnInfo(new ColumnInfo(DateValue.class, "exceldate"));

		xlsFile2.setFirstRowAsHeader(true);
		xlsFile2.addColumnInfo(new ColumnInfo(IntValue.class));
		xlsFile2.addColumnInfo(new ColumnInfo(StringValue.class));
		xlsFile2.addColumnInfo(new ColumnInfo(DateTimeValue.class));
		xlsFile2.addColumnInfo(new ColumnInfo(FloatValue.class));
		xlsFile2.addColumnInfo(new ColumnInfo(DateValue.class, "exceldate"));
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
	public void testReadXlsx2() throws Exception {
		DataTable table = xlsxFile2.createDataTable();

		List<DataColumn> columns = table.getColumns();
		assertEquals(IntValue.class, columns.get(0).getType());
		assertEquals(StringValue.class, columns.get(1).getType());
		assertEquals(DateTimeValue.class, columns.get(2).getType());
		assertEquals(FloatValue.class, columns.get(3).getType());
		assertEquals(DateValue.class, columns.get(4).getType());
		assertEquals(StringValue.class, columns.get(5).getType());

		assertEquals(3, table.getRowCount());
		DataRow row0 = table.getRow(0);
		DataRow row1 = table.getRow(1);
		DataRow row2 = table.getRow(2);

		assertEquals(new IntValue(1), row0.getValue(table.getColumn("Intheader")));
		assertEquals(new IntValue(8), row1.getValue(table.getColumn("Intheader")));
		assertTrue(row2.getValue(table.getColumn("Intheader")).isNull());

		assertEquals(new StringValue("row2"), row0.getValue(table.getColumn("Stringen")));
		assertEquals(new StringValue("row3"), row1.getValue(table.getColumn("Stringen")));
		assertEquals(new StringValue("dingen"), row2.getValue(table.getColumn("Stringen")));

		assertEquals(new DateTimeValue(1995, 9, 8, 9, 9, 9),
				row0.getValue(table.getColumn("Datums")));
		assertEquals(new DateTimeValue(1995, 12, 9, 13, 13, 13),
				row1.getValue(table.getColumn("Datums")));
		assertTrue(row2.getValue(table.getColumn("Datums")).isNull());

		assertEquals(new FloatValue(0.8f), row0.getValue(table.getColumn("Floots")));
		assertEquals(new FloatValue(13.13f), row1.getValue(table.getColumn("Floots")));
		assertEquals(new FloatValue(21.21f), row2.getValue(table.getColumn("Floots")));

		assertEquals(new DateValue(2012, 6, 20), row0.getValue(table.getColumn("lelijke_excel_dates")));
		assertEquals(new DateValue(2012, 6, 21), row2.getValue(table.getColumn("lelijke_excel_dates")));

		assertEquals(new StringValue("xlsx2"), row1.getValue(table.getColumn("M374D474")));


	}

	@Test
	public void testReadXls2() throws Exception {
		DataTable table = xlsFile2.createDataTable();

		List<DataColumn> columns = table.getColumns();
		assertEquals(IntValue.class, columns.get(0).getType());
		assertEquals(StringValue.class, columns.get(1).getType());
		assertEquals(DateTimeValue.class, columns.get(2).getType());
		assertEquals(FloatValue.class, columns.get(3).getType());

		assertEquals(3, table.getRowCount());
		DataRow row0 = table.getRow(0);
		DataRow row1 = table.getRow(1);
		DataRow row2 = table.getRow(2);

		assertEquals(new IntValue(1), row0.getValue(table.getColumn("Intheader")));
		assertEquals(new IntValue(8), row1.getValue(table.getColumn("Intheader")));
		assertTrue(row2.getValue(table.getColumn("Intheader")).isNull());

		assertEquals(new StringValue("row2"), row0.getValue(table.getColumn("Stringen")));
		assertEquals(new StringValue("row3"), row1.getValue(table.getColumn("Stringen")));
		assertEquals(new StringValue("dingen"), row2.getValue(table.getColumn("Stringen")));

		assertEquals(new DateTimeValue(1995, 9, 8, 9, 9, 9),
				row0.getValue(table.getColumn("Datums")));
		assertEquals(new DateTimeValue(1995, 12, 9, 13, 13, 13),
				row1.getValue(table.getColumn("Datums")));
		assertTrue(row2.getValue(table.getColumn("Datums")).isNull());

		assertEquals(new FloatValue(0.8f), row0.getValue(table.getColumn("Floots")));
		assertEquals(new FloatValue(13.13f), row1.getValue(table.getColumn("Floots")));
		assertEquals(new FloatValue(21.21f), row2.getValue(table.getColumn("Floots")));

		assertEquals(new DateValue(2012, 6, 20), row0.getValue(table.getColumn("lelijke_excel_dates")));
		assertEquals(new DateValue(2012, 6, 21), row2.getValue(table.getColumn("lelijke_excel_dates")));
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
