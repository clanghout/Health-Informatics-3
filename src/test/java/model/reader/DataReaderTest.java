package model.reader;

import model.data.DataTable;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * The tests for the DataReader.
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReaderTest {

	@Test
	public void testReadData() throws Exception {
		String text = "Nonsense\n" +
				"Bla bla\n" +
				"[\n" +
				"Crea,\t179,umol/L,00,130218,0802\n" +
				"Crea,\t179,umol/L,00,130218,0803\n" +
				"Crea,\t179,umol/L,00,130218,0804\n" +
				"Crea,\t179,umol/L,00,130218,0805\n" +
				"Crea,\t179,umol/L,00,130218,0806\n" +
				"]";
		InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));


		DataReader reader = new DataReader();
		DataTable model = reader.readData(stream);

		assertEquals(5, model.getRowCount());
	}
}