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
public class DateValueTest {

	@Test
	public void testGetValue() throws Exception {
		DataValue<Calendar> value = new DateValue(2011, 6, 18);
		assertEquals(new GregorianCalendar(2011, 5, 18), value.getValue());
	}

	@Test
	public void testToString() throws Exception {
		DataValue<Calendar> value = new DateValue(2011, 7, 18);
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
	public void testEqualsFormat() throws Exception {
		DataValue value = new DateValue(2019, 4, 4);
		DateValue notsame = new DateValue(2019, 4, 4);
		notsame.setSimpleDateFormat("dd/mm/yyyy");
		assertFalse(value.equals(notsame));
	}

	@Test
	public void testHashCode() throws Exception {
		DataValue value = new DateValue(2019, 4, 4);
		DataValue same = new DateValue(2019, 4, 4);
		assertEquals(value.hashCode(),same.hashCode());
	}
}