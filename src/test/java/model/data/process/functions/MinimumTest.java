package model.data.process.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataValue;
import model.data.process.functions.Minimum;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Louis Gosschalk
 * @before inspired from DataModelTest @author Jens
 * 12-05-2015
 * Test for minimum
 */
public class MinimumTest {
	
	private List<DataRow> rows;
	private DataColumn[] columns;
	private DataModel dataModel;
	
	/**
	 * simulate datamodel with single minimum for each column type
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		rows = new ArrayList<DataRow>();
		columns = new DataColumn[] {
				new DataColumn("stringcol", StringValue.class),
				new DataColumn("floatcol", FloatValue.class),
				new DataColumn("floatscol", FloatValue.class),
				new DataColumn("intcol", IntValue.class),
				new DataColumn("intscol", IntValue.class)
		};

		DataValue[] valuesRow1 = {
				new StringValue("This"),
				new FloatValue(6.8f),
				new FloatValue(6.8f),
				new IntValue(5),
				new IntValue(5)
		};

		DataValue[] valuesRow2 = {
				new StringValue("Is"),
				new FloatValue(6.9f),
				new FloatValue(6.6f),
				new IntValue(9),
				new IntValue(4)
		};

		DataValue[] valuesRow3 = {
				new StringValue("Sparta"),
				new FloatValue(6.6f),
				new FloatValue(6.6f),
				new IntValue(4),
				new IntValue(4)
		};
		rows.add(new DataRow(columns, valuesRow1));
		rows.add(new DataRow(columns, valuesRow2));
		rows.add(new DataRow(columns, valuesRow3));

		dataModel = new DataModel(rows, Arrays.asList(columns));
	}
	/**
	 * column of strings should return empty list of minimums
	 * @throws Exception
	 */
	@Test
	public void testStringMinimum() throws Exception {
		List<DataRow> minimum = new Minimum(dataModel,"stringcol").minimumCheck();
		List<DataRow> compare = new ArrayList<DataRow>();
		assertEquals(compare, minimum);
	}
	@Test
	public void testFloatMinimum() throws Exception {
		List<DataRow> minimum = new Minimum(dataModel,"floatcol").minimumCheck();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, minimum.get(0).getValue("floatcol"));
	}
	@Test
	public void testIntMinimum() throws Exception {
		List<DataRow> minimum = new Minimum(dataModel,"intcol").minimumCheck();
		IntValue i = new IntValue(4);
		assertEquals(i, minimum.get(0).getValue("intcol"));
	}
	@Test
	public void testFloatMultipleMinimum() throws Exception {
		List<DataRow> minimum = new Minimum(dataModel,"floatscol").minimumCheck();
		FloatValue f = new FloatValue(6.6f);
		assertEquals(f, minimum.get(0).getValue("floatscol"));
		assertEquals(f, minimum.get(1).getValue("floatscol"));
	}
	@Test
	public void testIntMultipleMinimum() throws Exception {
		List<DataRow> minimum = new Minimum(dataModel,"intscol").minimumCheck();
		IntValue i = new IntValue(4);
		assertEquals(i, minimum.get(0).getValue("intscol"));
		assertEquals(i, minimum.get(1).getValue("intscol"));
	}
}
