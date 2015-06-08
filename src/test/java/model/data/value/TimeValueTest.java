package model.data.value;

import model.data.value.DataValue;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by Chris on 11-5-2015.
 */
public class TimeValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue<Calendar> value = new TimeValue(9, 56, 12);
		assertEquals(new GregorianCalendar(0, 0, 0, 9, 56, 12), value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue<Calendar> value = new TimeValue(12, 6, 55);
		assertEquals("12:06:55", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		DataValue value = new TimeValue(4, 4, 4);
		DataValue same = new TimeValue(4, 4, 4);
		DataValue other = new TimeValue(4, 4, 5);
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
		assertFalse(value.equals(new IntValue(12)));
	}

	@Test
	public void testEqualsFormat() throws Exception {
		DataValue value = new TimeValue(4, 4, 4);
		TimeValue notSame = new TimeValue(4, 4, 4);
		notSame.setSimpleDateFormat("hh-mm-ss");
		assertFalse(value.equals(notSame));
	}

	@Test
	public void testHashCode() throws Exception {
		DataValue value = new TimeValue(12, 12, 12);
		DataValue same = new TimeValue(12, 12, 12);
		assertEquals(value.hashCode(), same.hashCode());
	}
}