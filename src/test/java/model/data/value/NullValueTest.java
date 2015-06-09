package model.data.value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 4-6-2015.
 *  Remade by Louis on 9-6-2015.
 */
public class NullValueTest {
	private DataValue value;


	@Test
	public void testNullBool() throws Exception {
		value = new BoolValue(null);
		assertEquals(false, value.getValue());
		assertEquals(true, value.isNull());
	}


	@Test
	public void testNullDateTime() throws Exception {
		value = new DateTimeValue(null,null,null,null,null,null);
		LocalDateTime compare = LocalDateTime.of(0, 1, 1, 0, 0, 0);
		assertEquals(compare, value.getValue());
		assertEquals(true, value.isNull());
	}

	@Test
	public void testNullDate() throws Exception {
		value = new DateValue(null,null,null);
		LocalDate compare = LocalDate.of(0, 1, 1);
		assertEquals(compare, value.getValue());
		assertEquals(true, value.isNull());
	}
	
	@Test
	public void testNullFloat() throws Exception {
		value = new FloatValue(null);
		assertEquals(0.0f, value.getValue());
		assertEquals(true, value.isNull());
	}
	
	@Test
	public void testNullInt() throws Exception {
		value = new IntValue(null);
		assertEquals(0, value.getValue());
		assertEquals(true, value.isNull());
	}
	
	@Test
	public void testNullString() throws Exception {
		value = new StringValue(null);
		assertEquals("", value.getValue());
		assertEquals(true, value.isNull());
	}
	
	@Test
	public void testNullTime() throws Exception {
		value = new TimeValue(null,null,null);
		LocalTime compare = LocalTime.of(0, 0, 0);
		assertEquals(compare, value.getValue());
		assertEquals(true, value.isNull());
	}
}