package model.process.analysis.operations.constraints;

import model.data.describer.ConstantDescriber;
import model.data.value.BoolValue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains the test cases for the NotCheck
 * Created by Boudewijn on 12-5-2015.
 */
public class NotCheckTest {

	@Test
	public void testCheckTrue() throws Exception {
		NotCheck check = new NotCheck(new ConstantDescriber<>(new BoolValue(true)));
		assertFalse(check.check(null));
	}

	@Test
	public void testCheckFalse() throws Exception {
		NotCheck check = new NotCheck(new ConstantDescriber<>(new BoolValue(false)));
		assertTrue(check.check(null));
	}
}