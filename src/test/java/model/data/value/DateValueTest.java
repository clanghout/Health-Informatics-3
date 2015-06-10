package model.data.value;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by Chris on 11-5-2015.
 */
public class DateValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue<LocalDate> value = new DateValue(2011, 6, 18);
		assertEquals(LocalDate.of(2011, 6, 18), value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue<LocalDate> value = new DateValue(2011, 7, 18);
		assertEquals("18-07-2011", value.toString());
	}

	@Test
	public void testEquals() throws Exception {
		DataValue value = new DateValue(2019, 4, 4);
		DataValue same = new DateValue(2019, 4, 4);
		DataValue other = new DateValue(2019, 4, 5);
		assertTrue(value.equals(same));
		assertFalse(value.equals(other));
		assertFalse(value.equals(new IntValue(12)));
	}

	@Test
	public void testHashCode() throws Exception {
		DataValue value = new DateValue(2019, 4, 4);
		DataValue same = new DateValue(2019, 4, 4);
		assertEquals(value.hashCode(),same.hashCode());
	}
}