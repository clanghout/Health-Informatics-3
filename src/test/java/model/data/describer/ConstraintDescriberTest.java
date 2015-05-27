package model.data.describer;

import model.process.analysis.operations.constraints.Constraint;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.data.value.BoolValue;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * A class containing the test cases for the ConstraintDescriber.
 * Created by Boudewijn on 11-5-2015.
 */
public class ConstraintDescriberTest {

	@Test
	public void testResolve() throws Exception {
		Constraint check = new EqualityCheck<>(
				new ConstantDescriber<>(new BoolValue(true)),
				new ConstantDescriber<>(new BoolValue(true))
		);
		ConstraintDescriber describer = new ConstraintDescriber(check);
		assertTrue(describer.resolve(null).getValue());
	}
}