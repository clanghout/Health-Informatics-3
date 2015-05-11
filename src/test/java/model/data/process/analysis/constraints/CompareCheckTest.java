package model.data.process.analysis.constraints;

import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Contains the test cases for all the comparison checks
 * Created by Boudewijn on 11-5-2015.
 */
@RunWith(Parameterized.class)
public class CompareCheckTest {



	public boolean expected;

	private CompareCheck<IntValue> check;

	public CompareCheckTest(
			Class<? extends CompareCheck> check,
			DataDescriber<DataValue> left,
			DataDescriber<DataValue> right,
			boolean expected) throws Exception{
		this.check = check.getDeclaredConstructor(DataDescriber.class, DataDescriber.class)
				.newInstance(left, right);
		this.expected = expected;
	}

	@Test
	public void testCompare() throws Exception {
		assertEquals(expected, check.check(null));
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		DataDescriber<IntValue> five = new ConstantDescriber<>(new IntValue(5));
		DataDescriber<IntValue> seven = new ConstantDescriber<>(new IntValue(7));
		
		
		DataDescriber<FloatValue> four = new ConstantDescriber<>(new FloatValue(4.0f));
		DataDescriber<FloatValue> six = new ConstantDescriber<>(new FloatValue(6.0f));

		List<Object[]> intTests = Arrays.asList(new Object[][]{
				{GreaterThanCheck.class, five, seven, false},
				{GreaterThanCheck.class, five, five, false},
				{GreaterThanCheck.class, seven, five, true},
				{LesserThanCheck.class, five, seven, true},
				{LesserThanCheck.class, five, five, false},
				{LesserThanCheck.class, seven, five, false},
				{GreaterEqualsCheck.class, five, seven, false},
				{GreaterEqualsCheck.class, five, five, true},
				{GreaterEqualsCheck.class, seven, five, true},
				{LesserEqualsCheck.class, five, seven, true},
				{LesserEqualsCheck.class, five, five, true},
				{LesserEqualsCheck.class, seven, five, false}
		});
		List<Object[]> floatTests = Arrays.asList(new Object[][]{
				{GreaterThanCheck.class, four, six, false},
				{GreaterThanCheck.class, four, four, false},
				{GreaterThanCheck.class, six, four, true},
				{LesserThanCheck.class, four, six, true},
				{LesserThanCheck.class, four, four, false},
				{LesserThanCheck.class, six, four, false},
				{GreaterEqualsCheck.class, four, six, false},
				{GreaterEqualsCheck.class, four, four, true},
				{GreaterEqualsCheck.class, six, four, true},
				{LesserEqualsCheck.class, four, six, true},
				{LesserEqualsCheck.class, four, four, true},
				{LesserEqualsCheck.class, six, four, false}
		});
		List<Object[]> tests = new ArrayList<>(intTests);
		tests.addAll(floatTests);
		return tests;
	}

}