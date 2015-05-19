package model.data.process.analysis.operations.constraints;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.describer.ConstantDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The tests for the EqualityCheckTest
 * Created by Boudewijn on 5-5-2015.
 */
public class EqualityCheckTest {

	private EqualityCheck<StringValue> hankCheck;
	private DataColumn column;

	@Before
	public void setUp() throws Exception {
		column = new DataColumn("test", StringValue.class);

		hankCheck = new EqualityCheck<>(
				new RowValueDescriber<>(column),
				new ConstantDescriber<>(new StringValue("Hank")
				)
		);
	}

	@Test
	public void testCheckPass() throws Exception {
		DataRow row = new DataRow();
		DataValue value = new StringValue("Hank");

		row.setValue(column, value);


		assertTrue(hankCheck.check(row));
	}

	@Test
	public void testCheckFail() {
		DataRow secondRow = new DataRow();
		DataValue otherValue = new StringValue("John");


		secondRow.setValue(column, otherValue);

		assertFalse(hankCheck.check(secondRow));
	}
}