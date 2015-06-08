package model.data.value;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 11-5-2015.
 */
public class FloatValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue value = new FloatValue(12.6f);
		assertEquals(12.6f, value.getValue());
		value = new FloatValue(0f);
		assertEquals(0f, value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue value = new FloatValue(123.456f);
		assertEquals("123.456", value.toString());
		value = new FloatValue(0f);
		assertEquals("0.0", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		DataValue value = new FloatValue(789.12345f);
		DataValue same = new FloatValue(789.12345f);
		DataValue other = new FloatValue(37289156.326155f);
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
		assertFalse(value.equals(new IntValue(12)));
	}

	@Test
	public void testHashCode() throws Exception {
		Float val = 2335f;
		DataValue value = new FloatValue(val);
		DataValue samevalue = new FloatValue(val);
		assertEquals(samevalue.hashCode(),value.hashCode());
	}

	@Test
	public void testcopy() throws Exception {
		Float val = 2335f;
		DataValue value = new FloatValue(val);
		DataValue copy = value.copy();
		assertEquals(copy.getValue(),value.getValue());
		assertEquals(copy.getClass(),value.getClass());
	}
}