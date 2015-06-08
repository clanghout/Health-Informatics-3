package model.data.value;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by Chris on 11-5-2015.
 */
public class DateTimeValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue<LocalDateTime> value = new DateTimeValue(2011, 6, 18, 15, 12, 40);
		assertEquals(LocalDateTime.of(2011, 6, 18, 15, 12, 40), value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue<LocalDateTime> value = new DateTimeValue(2011, 7, 18, 0, 0, 0);
		assertEquals("18-07-2011 00:00:00", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		DataValue value = new DateTimeValue(2019, 4, 4, 12, 36, 12);
		DataValue same = new DateTimeValue(2019, 4, 4, 12, 36, 12);
		DataValue other = new DateTimeValue(2019, 4, 4, 12, 36, 13);
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
		assertFalse(value.equals(new IntValue(12)));
	}

	@Test
	public void testHashCode() throws Exception {
		DataValue value = new DateTimeValue(2019, 4, 4, 12,36,12);
		DataValue same = new DateTimeValue(2019, 4, 4, 12,36,12);
		assertEquals(value.hashCode(), same.hashCode());
	}

	@Test
	public void testCopy() throws Exception {
		DataValue value = new DateTimeValue(2019, 4, 4, 12,36,12);
		DataValue copy = value;
		assertEquals(copy.getValue(),value.getValue());
		assertEquals(copy.getClass(),value.getClass());
	}
}