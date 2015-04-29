package model.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReader {

	private Logger log = Logger.getLogger("DataReader");

	public DataModel readData(String filename) throws IOException {
		File file = new File(filename);
		return readData(file);
	}

	public DataModel readData(File file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

		} catch (IOException e) {
			log.throwing(this.getClass().getSimpleName(), "readData", e);
			throw e;
		}
	}
}
