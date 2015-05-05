package model.data.process.analysis.constraints;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataValue;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Boudewijn on 5-5-2015.
 */
public class EqualityCheckTest {

	@Test
	public void testCheck() throws Exception {
		DataRow row = new DataRow();
		DataColumn column = new DataColumn("test", StringValue.class);
		DataValue value = new StringValue("Hank");
		row.setValue(column, value);

		EqualityCheck check = new EqualityCheck(column, value);
		assertTrue(check.check(row));

		DataRow secondRow = new DataRow();
		DataValue otherValue = new StringValue("John");
		secondRow.setValue(column, otherValue);

		assertFalse(check.check(secondRow));
	}
}