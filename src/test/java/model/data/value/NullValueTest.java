package model.data.value;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Louis Gosschalk on 8-6-2015.
 */
public class NullValueTest {
	private DataValue value;
		
	@Test
	public void testGetValueBool() throws Exception {
		value = new BoolValue(null);
		assertEquals(false, value.getValue());
	}
	
	@Test
	public void testGetValueDateTime() throws Exception {
		value = new DateTimeValue(null, null, null, null, null, null);
		DataValue compare = new DateTimeValue(0,1,1,0,0,0);
		assertEquals(compare.getValue(), value.getValue());
	}
	
	@Test
	public void testGetValueDate() throws Exception {
		value = new DateValue(null);
		assertEquals(null, value.getValue());
	}
	
	@Test
	public void testGetValueDate2() throws Exception {
		value = new DateValue(null,null,null);
		DataValue compare = new DateValue(0,1,1);
		assertEquals(compare.getValue(), value.getValue());
	}
	
	@Test
	public void testGetValueTime() throws Exception {
		value = new TimeValue(null,null,null);
		DataValue compare = new TimeValue(0,0,0);
		assertEquals(compare.getValue(), value.getValue());
	}
	
	@Test
	public void testGetValueFloat() throws Exception {
		value = new FloatValue(null);
		assertEquals(0f, value.getValue());
	}
	
	@Test
	public void testGetValueInt() throws Exception {
		value = new IntValue(null);
		assertEquals(0, value.getValue());
	}
	
	@Test
	public void testGetValueString() throws Exception {
		value = new StringValue(null);
		assertEquals("", value.getValue());
	}
}