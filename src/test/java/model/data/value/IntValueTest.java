package model.data.value;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 11-5-2015.
 */
public class IntValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue value = new IntValue(12);
		assertEquals(12, value.getValue());
		value = new IntValue(0);
		assertEquals(0, value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue value = new IntValue(123);
		assertEquals("123", value.toString());
		value = new IntValue(0);
		assertEquals("0", value.toString());
		value = new IntValue(-177);
		assertEquals("-177", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		DataValue value = new IntValue(789);
		DataValue same = new IntValue(789);
		DataValue other = new IntValue(37289156);
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
		assertFalse(value.equals(new FloatValue(1f)));
	}

	@Test
	public void testHashCode() throws Exception {
		DataValue value = new IntValue(24897);
		assertEquals(24897,value.hashCode());
	}

	@Test
	public void testcopy() throws Exception {
		DataValue value = new IntValue(24897);
		DataValue copy = value.copy();
		assertEquals(copy.getValue(),value.getValue());
		assertEquals(copy.getClass(),value.getClass());
	}
}