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
public class DateTimeValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue<Calendar> value = new DateTimeValue(2011, 6, 18, 15, 12, 40);
		// month values are different because when we think of month 6, it's june.
		// The calendar makes this july because the calendar states january is month 0.
		assertEquals(new GregorianCalendar(2011, 5, 18, 15, 12, 40), value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue<Calendar> value = new DateTimeValue(2011, 7, 18, 0, 0, 0);
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
	public void testEqualsDateFormat() throws Exception {
		DataValue value = new DateTimeValue(2019, 4, 4, 12, 36, 12);
		DateTimeValue same = new DateTimeValue(2019, 4, 4, 12, 36, 12);
		same.setSimpleDateFormat("mm/dd/yyyy hh:mm ss");
		assertFalse(value.equals(same));
	}

	@Test
	public void testHashCode() throws Exception {
		DataValue value = new DateTimeValue(2019, 4, 4, 12,36,12);
		DataValue same = new DateTimeValue(2019, 4, 4, 12,36,12);
		assertEquals(value.hashCode(), same.hashCode());
	}

	@Test
	public void testcopy() throws Exception {
		DataValue value = new DateTimeValue(2019, 4, 4, 12,36,12);
		DataValue copy = value.copy();
		assertEquals(copy.getValue(),value.getValue());
		assertEquals(copy.getClass(),value.getClass());
	}
}