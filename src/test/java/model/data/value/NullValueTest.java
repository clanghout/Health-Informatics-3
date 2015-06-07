package model.data.value;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 4-6-2015.
 */
public class NullValueTest {
	private NullValue value;

	@Before
	public void setUp() throws Exception {
		value = new NullValue();
	}

	@Test
	public void testGetValue() throws Exception {
		assertEquals(null, value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		assertEquals("null", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		NullValue same = new NullValue();
		DataValue other = new StringValue("");
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
	}

	@Test
	public void testHashCode() throws Exception {
		assertEquals(0, value.hashCode());
	}
}