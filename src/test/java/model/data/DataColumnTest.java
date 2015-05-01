package model.data;

import model.data.data.value.StringValue;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by jens on 4/29/15.
 */
public class DataColumnTest {

	@Test
	 public void testGetName() throws Exception {
		DataColumn column = new DataColumn("test", StringValue.class);
		assertEquals(column.getName(), "test");
	}

	@Test
	public void testGetType() throws Exception {
		DataColumn column = new DataColumn("test", StringValue.class);
		assertEquals(column.getType(), StringValue.class);
	}
}