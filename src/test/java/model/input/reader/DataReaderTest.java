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
		DataTable table = model.getByName("xlsfilexls").get();
		Row theRow = table.getRow(1);
		assertEquals("bat", (theRow.getValue(table.getColumn("thecolumn"))).getValue());

		DataTable table2 = model.getByName("ADMIRE2txt").get();
		Row theRow2 = table2.getRow(0);
		Row theRow3 = table2.getRow(1);

		assertEquals("ADMIRE2", (theRow2.getValue(table2.getColumn("metadata"))).getValue());
		assertEquals("ADMIRE2", (theRow3.getValue(table2.getColumn("metadata"))).getValue());
	}
	
	@Test(expected = IOException.class)
	public void testReadEmptyTextFile() throws Exception{
		File file = new File(getClass().getResource("/emptytext_save.xml").getFile());
		DataReader reader = new DataReader(file);
		DataModel model = reader.createDataModel();
	}
}