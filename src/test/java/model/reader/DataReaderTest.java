package model.reader;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReaderTest extends TestCase {

	@Test
	public void testReadData() throws Exception {
		URL url = getClass().getResource("/ADMIRE2.txt");
		File file = new File(url.getPath());
		DataReader reader = new DataReader();
		reader.readData(file);
	}
}