package model.output;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataRow;
import model.data.value.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Chris on 6-5-2015.
 */
public class DataTableWriterTest {
	private DataTable dataTable;
	private File testFile;
	private List<DataRow> rows;
	private DataColumn[] columns;

	@Before
	public void setUp() throws Exception {
		dataTable = mock(DataTable.class);
		testFile = new File("testFile.txt");
	}

	@Test
	public void testFile() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class),
				new DataColumn("column2", null, StringValue.class),
				new DataColumn("column3", null, StringValue.class)
		};

		DataValue[] valuesRow1 = {
				new StringValue("value1"),
				new StringValue("value2"),
				new StringValue("value3")
		};

		DataValue[] valuesRow2 = {
				new StringValue("value1b"),
				new StringValue("value2b"),
				new StringValue("value3b")
		};

		DataValue[] valuesRow3 = {
				new StringValue("value1c"),
				new StringValue("value2c"),
				new StringValue("value3c")
		};
		rows.add(new DataRow(columns, valuesRow1));
		rows.add(new DataRow(columns, valuesRow2));
		rows.add(new DataRow(columns, valuesRow3));

		ArrayList<DataColumn> columnsMap = new ArrayList<>(Arrays.asList(this.columns));

		when(dataTable.getRows()).thenReturn(unmodifiableList(rows));
		when(dataTable.getColumns()).thenReturn(columnsMap);

		DataTableWriter writer = new DataTableWriter();
		writer.setDelimiter(",");
		writer.write(dataTable, testFile);
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "value1,value2,value3");
		String secondLine = reader.readLine();
		assertEquals(secondLine, "value1b,value2b,value3b");
		String thirdLine = reader.readLine();
		assertEquals(thirdLine, "value1c,value2c,value3c");
	}

	@Test
	public void testDelimiter() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class),
				new DataColumn("column2", null, StringValue.class),
				new DataColumn("column3", null, StringValue.class)
		};
		DataValue[] valuesRow1 = {
				new StringValue("value1"),
				new StringValue("value2"),
				new StringValue("value3")
		};
		rows.add(new DataRow(columns, valuesRow1));

		ArrayList<DataColumn> columnsMap = new ArrayList<>(Arrays.asList(this.columns));

		when(dataTable.getRows()).thenReturn(unmodifiableList(rows));
		when(dataTable.getColumns()).thenReturn(columnsMap);

		DataTableWriter writer = new DataTableWriter();
		writer.setDelimiter("\t");
		writer.write(dataTable, testFile);
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "value1\tvalue2\tvalue3");
		writer.setDelimiter(" ");
		writer.write(dataTable, testFile);
		reader = new BufferedReader(new FileReader(testFile));
		firstLine = reader.readLine();
		assertEquals(firstLine, "value1 value2 value3");
	}

	@Test
	public void testAddQuotes() throws Exception {
		DataTableWriter writer = new DataTableWriter();
		writer.setQuotationMarks(true);
		String test = writer.formatValue(new StringValue("test"));
		assertEquals(test, "\"test\"");
	}

	@Test
	public void testNULL() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class)
		};
		DataValue[] valuesRow1 = {
				new StringValue(null)
		};
		rows.add(new DataRow(columns, valuesRow1));

		ArrayList<DataColumn> columnsMap = new ArrayList<>(Arrays.asList(this.columns));

		when(dataTable.getRows()).thenReturn(unmodifiableList(rows));
		when(dataTable.getColumns()).thenReturn(columnsMap);

		DataTableWriter writer = new DataTableWriter();
		writer.setNullVal("NULL");
		writer.write(dataTable, testFile);
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "NULL");
		writer.setNullVal("null");
		writer.write(dataTable, testFile);
		reader = new BufferedReader(new FileReader(testFile));
		firstLine = reader.readLine();
		assertEquals(firstLine, "null");
		writer.setNullVal("");
		writer.write(dataTable, testFile);
		reader = new BufferedReader(new FileReader(testFile));
		firstLine = reader.readLine();
		assertEquals(firstLine, "");
	}

	@Test
	 public void testDate() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", null, DateValue.class)
		};
		DataValue[] valuesRow1 = {
				new DateValue(2000,12,12)
		};
		rows.add(new DataRow(columns, valuesRow1));

		ArrayList<DataColumn> columnsMap = new ArrayList<>(Arrays.asList(this.columns));

		when(dataTable.getRows()).thenReturn(unmodifiableList(rows));
		when(dataTable.getColumns()).thenReturn(columnsMap);

		DataTableWriter writer = new DataTableWriter();
		writer.write(dataTable, testFile);
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "2000-12-12");
	}

	@Test
	public void testDateTime() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", null, DateTimeValue.class)
		};
		DataValue[] valuesRow1 = {
				new DateTimeValue(2000,12,12, 8, 45,12)
		};
		rows.add(new DataRow(columns, valuesRow1));

		ArrayList<DataColumn> columnsMap = new ArrayList<>(Arrays.asList(this.columns));

		when(dataTable.getRows()).thenReturn(unmodifiableList(rows));
		when(dataTable.getColumns()).thenReturn(columnsMap);

		DataTableWriter writer = new DataTableWriter();
		writer.write(dataTable, testFile);
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "2000-12-12 8:45:12");
	}

	@Test
	public void testCsvWrite() throws Exception {
		rows = new ArrayList<>();
		ArrayList<DataColumn> columnsMap = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", null, StringValue.class),
				new DataColumn("column2", null, IntValue.class)
		};
		columnsMap.add(columns[0]);
		columnsMap.add(columns[1]);


		DataValue[] valuesRow1 = {
				new StringValue("test"),
				new IntValue(12)
		};
		DataValue[] valuesRow2 = {
				new StringValue("test2"),
				new IntValue(55)
		};
		rows.add(new DataRow(columns, valuesRow1));
		rows.add(new DataRow(columns, valuesRow2));

		when(dataTable.getRows()).thenReturn(unmodifiableList(rows));
		when(dataTable.getColumns()).thenReturn(columnsMap);

		DataTableWriter writer = new DataTableWriter();
		writer.writeCSV(dataTable, testFile);
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();

		assertEquals("test,12", firstLine);

		String secondLine = reader.readLine();

		assertEquals("test2,55", secondLine);

	}

}
