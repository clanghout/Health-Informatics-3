package model.process.describer;

import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test for the ConstantDescriber.
 * Created by Boudewijn on 11-5-2015.
 */
public class ConstantDescriberTest {

	@Test
	public void testResolve() throws Exception {
		ConstantDescriber<StringValue> describer =
				new ConstantDescriber<>(new StringValue("Hello"));
		assertEquals(new StringValue("Hello"), describer.resolve(null));
	}
}