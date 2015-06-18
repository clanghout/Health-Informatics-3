package model.BackgroundProcesses;

import model.input.reader.DataReader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jens on 6/17/15.
 */
public class Reader implements Runnable {
	private Logger logger = Logger.getLogger("Reader");

	private final File file;

	public Reader(File file) {
		this.file = file;
	}

	@Override
	public void run() {
		try {
			DataReader reader = new DataReader(file);
			reader.createDataModel();
			logger.info("done reading files");
		} catch (Exception e) {
			logger.log(Level.WARNING, "An error occurred while reading the file", e);
		}
	}
}
