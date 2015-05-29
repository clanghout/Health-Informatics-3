package model.input.reader;

import model.data.DataModel;
import model.data.DataTable;
import model.input.reader.DataReader;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * The tests for the DataReader.
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReaderTest {

	@Test(expected = IOException.class)
	public void testReaderFail() throws Exception {
		File file = new File(getClass().getResource("/user_save.xml").getFile());
		
		DataReader reader = new DataReader(file);
		DataModel model = reader.createDataModel();		
	}
	
	@Test
	public void testReaderPass() throws Exception {
		File ideal = new File(getClass().getResource("/user_save2.xml").getFile());

		DataReader reader = new DataReader(ideal);
		DataModel model = reader.createDataModel();		
		assertEquals(3, model.size());
		
	}
}