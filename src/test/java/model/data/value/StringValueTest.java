package model.data.value;

import model.data.DataValue;
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
        assertEquals("StringValue<abc>", value.toString());
        value = new StringValue("");
        assertEquals("StringValue<>", value.toString());
    }
}