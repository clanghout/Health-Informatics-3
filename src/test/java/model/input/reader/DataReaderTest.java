package model.input.reader;

import model.data.DataModel;
import model.data.DataTable;
import model.data.Row;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * The tests for the DataReader.
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReaderTest {
	
	@Test
	public void testReaderPass() throws Exception {

		File file = new File(getClass().getResource("/user_save2.xml").getFile());

		DataReader reader = new DataReader(file);
		DataModel model = reader.createDataModel();
		
		assertEquals(3, model.size());
		DataTable table = model.getByName("xlsfilexls");
		Row theRow = table.getRow(1);
		assertEquals("bat", (theRow.getValue(table.getColumn("thecolumn"))).getValue());
	}
	
	@Test(expected = IOException.class)
	public void testReadEmptyTextFile() throws Exception{
		File file = new File(getClass().getResource("/emptytext_save.xml").getFile());
		DataReader reader = new DataReader(file);
		DataModel model = reader.createDataModel();
	}
}