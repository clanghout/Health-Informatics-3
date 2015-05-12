package model.data.process.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataValue;
import model.data.process.functions.Maximum;
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
 * Test for maximum
 */
public class MaximumTest {
	
	private List<DataRow> rows;
	private DataColumn[] columns;
	private DataModel dataModel;
	
	/**
	 * simulate datamodel with single maximum for each column type
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
				new FloatValue(6.9f),
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
				new FloatValue(6.9f),
				new IntValue(4),
				new IntValue(5)
		};
		rows.add(new DataRow(columns, valuesRow1));
		rows.add(new DataRow(columns, valuesRow2));
		rows.add(new DataRow(columns, valuesRow3));

		dataModel = new DataModel(rows, Arrays.asList(columns));
	}
	/**
	 * column of strings should return empty list of maximums
	 * @throws Exception
	 */
	@Test
	public void testStringMaximum() throws Exception {
		List<DataRow> maximum = new Maximum(dataModel,"stringcol").maximumCheck();
		List<DataRow> compare = new ArrayList<DataRow>();
		assertEquals(compare, maximum);
	}
	@Test
	public void testFloatMaximum() throws Exception {
		List<DataRow> maximum = new Maximum(dataModel,"floatcol").maximumCheck();
		FloatValue f = new FloatValue(6.9f);
		assertEquals(f, maximum.get(0).getValue("floatcol"));
	}
	@Test
	public void testIntMaximum() throws Exception {
		List<DataRow> maximum = new Maximum(dataModel,"intcol").maximumCheck();
		IntValue i = new IntValue(9);
		assertEquals(i, maximum.get(0).getValue("intcol"));
	}
	@Test
	public void testFloatMultipleMaximum() throws Exception {
		List<DataRow> maximum = new Maximum(dataModel,"floatscol").maximumCheck();
		FloatValue f = new FloatValue(6.9f);
		assertEquals(f, maximum.get(0).getValue("floatscol"));
		assertEquals(f, maximum.get(1).getValue("floatscol"));
	}
	@Test
	public void testIntMultipleMaximum() throws Exception {
		List<DataRow> maximum = new Maximum(dataModel,"intscol").maximumCheck();
		IntValue i = new IntValue(5);
		assertEquals(i, maximum.get(0).getValue("intscol"));
		assertEquals(i, maximum.get(1).getValue("intscol"));
	}
}
