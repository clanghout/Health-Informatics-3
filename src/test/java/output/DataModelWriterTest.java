package output;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Chris on 6-5-2015.
 */
public class DataModelWriterTest {
	private DataModel dataModel;
	private File testFile;
	private List<DataRow> rows;
	private DataColumn[] columns;

	@Before
	public void setUp() throws Exception {
		dataModel = mock(DataModel.class);
		testFile = new File("testFile.txt");
	}

	@Test
	public void testFile() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", StringValue.class),
				new DataColumn("column2", StringValue.class),
				new DataColumn("column3", StringValue.class)
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

		HashMap<String, DataColumn> columnsMap = new HashMap<>();
		for (DataColumn c : this.columns) {
			columnsMap.put(c.getName(), c);
		}
		when(dataModel.getRows()).thenReturn(unmodifiableList(rows));
		when(dataModel.getColumns()).thenReturn(columnsMap);

		DataModelWriter writer = new DataModelWriter();
		writer.write(dataModel, testFile, ",");
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "value1,value3,value2");
		String secondLine = reader.readLine();
		assertEquals(secondLine, "value1b,value3b,value2b");
		String thirdLine = reader.readLine();
		assertEquals(thirdLine, "value1c,value3c,value2c");
	}

	@Test
	public void testDelimiter() throws Exception {
		rows = new ArrayList<>();
		columns = new DataColumn[]{
				new DataColumn("column1", StringValue.class),
				new DataColumn("column2", StringValue.class),
				new DataColumn("column3", StringValue.class)
		};
		DataValue[] valuesRow1 = {
				new StringValue("value1"),
				new StringValue("value2"),
				new StringValue("value3")
		};
		rows.add(new DataRow(columns, valuesRow1));

		HashMap<String, DataColumn> columnsMap = new HashMap<>();
		for (DataColumn c : this.columns) {
			columnsMap.put(c.getName(), c);
		}
		when(dataModel.getRows()).thenReturn(unmodifiableList(rows));
		when(dataModel.getColumns()).thenReturn(columnsMap);

		DataModelWriter writer = new DataModelWriter();
		writer.write(dataModel, testFile, "\t");
		BufferedReader reader = new BufferedReader(new FileReader(testFile));
		String firstLine = reader.readLine();
		assertEquals(firstLine, "value1\tvalue3\tvalue2");
		writer.write(dataModel, testFile, " ");
		reader = new BufferedReader(new FileReader(testFile));
		firstLine = reader.readLine();
		assertEquals(firstLine, "value1 value3 value2");
	}

	@Test
	public void testAddQuotes() throws Exception {
		DataModelWriter writer = new DataModelWriter();
		String test = writer.addQuotes(new StringValue("test"));
		assertEquals(test, "\"test\"");
	}
}