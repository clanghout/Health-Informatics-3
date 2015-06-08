package model.data.value;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 29-4-2015.
 */
public class StringValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue value = new StringValue("abc");
		assertEquals("abc", value.getValue());
		value = new StringValue("");
		assertEquals("", value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue value = new StringValue("abc");
		assertEquals("abc", value.toString());
		value = new StringValue("");
		assertEquals("", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		DataValue value = new StringValue("abc");
		DataValue same = new StringValue("abc");
		DataValue other = new StringValue("oidsa");
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
		assertFalse(value.equals(new IntValue(12)));
	}

	@Test
	public void testHashcode() throws Exception {
		String val = "testen";
		DataValue value = new StringValue(val);
		DataValue samevalue = new StringValue(val);
		assertEquals(samevalue.hashCode(), value.hashCode());
	}

	@Test
	public void testcopy() throws Exception {
		String val = "testen";
		DataValue value = new StringValue(val);
		DataValue copy = value;
		assertEquals(copy.getValue(),value.getValue());
		assertEquals(copy.getClass(),value.getClass());
	}

}