package model.data.process.analysis.operations.constraints;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.ConstantDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.data.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/26/15.
 */
public class CodeCheckTest {

	@Test
	public void testCheck() throws Exception {
		DataRow row = new DataRow();
		row.addCode("empty");

		CodeCheck check = new CodeCheck(new ConstantDescriber<>(new StringValue("empty")));
		assertTrue(check.check(row));
	}

	@Test
	public void testCheckMultipleCodes() throws Exception {
		DataRow row = new DataRow();
		row.addCode("empty");
		row.addCode("test");
		row.addCode("test3");

		CodeCheck check = new CodeCheck(new ConstantDescriber<>(new StringValue("empty")));
		assertTrue(check.check(row));
		check = new CodeCheck(new ConstantDescriber<>(new StringValue("test")));
		assertTrue(check.check(row));
		check = new CodeCheck(new ConstantDescriber<>(new StringValue("test3")));
		assertTrue(check.check(row));
	}

	@Test
	public void testCheckFalse() throws Exception {
		DataRow row = new DataRow();
		row.addCode("empty");

		CodeCheck check = new CodeCheck(new ConstantDescriber<>(new StringValue("fail")));
		assertFalse(check.check(row));
	}

	@Test
	public void testCheckNoCodesFalse() throws Exception {
		DataRow row = new DataRow();

		CodeCheck check = new CodeCheck(new ConstantDescriber<>(new StringValue("fail")));
		assertFalse(check.check(row));
	}

	@Test
	public void testCheckEmptyString() throws Exception {
		DataRow row = new DataRow();

		CodeCheck check = new CodeCheck(new ConstantDescriber<>(new StringValue("")));
		assertFalse(check.check(row));
	}
}