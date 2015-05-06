package model.data;


import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by jens on 4/30/15.
 */
public class DataModelTest {
	private ArrayList<DataRow> rows;
	private DataColumn[] columns;
	private DataModel dataModel;


	@Before
	public void setUp() throws Exception {
		rows = new ArrayList<DataRow>();
		columns = new DataColumn[] {
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

		dataModel = new DataModel(rows, Arrays.asList(columns));
	}

	@Test
	public void testGetRow() throws Exception {
		assertEquals(dataModel.getRow(1), rows.get(1));
	}
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetNotExistingRow() throws Exception {
		dataModel.getRow(45);
	}

	@Test
	public void testGetRows() throws Exception {
		Iterator<DataRow> iteratorRowsModel = dataModel.getRows();
		Iterator<DataRow> iteratorRows = rows.iterator();
		while(iteratorRows.hasNext() || iteratorRowsModel.hasNext()) {
			assertEquals(iteratorRows.next(), iteratorRowsModel.next());
		}
	}

	@Test
	public void testGetColumns() throws Exception {
		Map<String, DataColumn> actual = dataModel.getColumns();
		for (DataColumn c : columns) {
			assertEquals(actual.get(c.getName()), c);
		}
	}
}