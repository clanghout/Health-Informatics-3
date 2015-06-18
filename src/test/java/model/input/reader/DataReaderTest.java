package model.input.reader;

import model.data.DataModel;
import model.data.DataTable;
import model.data.Row;

import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * The tests for the DataReader.
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReaderTest {
	
	@Test(expected = IOException.class)
	public void testReadEmptyTextFile() throws Exception{
		File file = new File(getClass().getResource("/emptytext_save.xml").getFile());
		DataReader reader = new DataReader(new XmlReader());
		reader.read(file);
		DataModel model = reader.createDataModel();
	}
}