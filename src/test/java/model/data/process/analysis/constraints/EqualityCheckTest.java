package model.data.process.analysis.constraints;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Boudewijn on 5-5-2015.
 */
public class EqualityCheckTest {

	private EqualityCheck hankCheck;
	private DataColumn column;

	@Before
	public void setUp() throws Exception {
		column = new DataColumn("test", StringValue.class);

		hankCheck = new EqualityCheck(column, new StringValue("Hank"));
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