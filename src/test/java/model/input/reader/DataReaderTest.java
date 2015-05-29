package model.input.reader;

import model.data.DataModel;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * The tests for the DataReader.
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReaderTest {

	@Test
	public void testReader() throws Exception {
		
		File file = new File(getClass().getResource("/user_save.xml").getFile());
		
		DataReader reader = new DataReader(file);
		DataModel model = reader.createDataModel();

		assertEquals(3, model.size());
	}
}