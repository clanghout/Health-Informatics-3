package model.data;


import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by jens on 4/30/15.
 */
public class DataModelTest {
	private ArrayList<DataRow> rows;
	private HashMap<String, DataColumn> columnsMap;
	private DataColumn[] columns;
	private DataModel dataModel;


	@Before
	public void setUp() throws Exception {
		rows = new ArrayList<DataRow>();
		columnsMap = new HashMap<String, DataColumn>();
		columns = new DataColumn[] {
				new DataColumn("column1", StringValue.class),
				new DataColumn("column2", StringValue.class),
				new DataColumn("column3", StringValue.class)
		};

		columnsMap.put("column1", columns[0]);
		columnsMap.put("column2", columns[1]);
		columnsMap.put("column3", columns[2]);

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

		dataModel = new DataModel(rows, columnsMap);
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
		assertEquals(dataModel.getColumns(), columnsMap);
	}
}