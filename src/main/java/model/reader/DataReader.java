package model.reader;

import model.data.DataModel;
import model.data.DataModelBuilder;
import model.data.DataRow;
import model.data.DataValue;
import model.data.data.value.StringValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
			skipToContent(reader);
			DataModel model = readRows(reader);


			return model;
		} catch (IOException e) {
			log.throwing(this.getClass().getSimpleName(), "readData", e);
			throw e;
		}
	}

	private void skipToContent(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()).indexOf("[") == -1) {}
	}

	private DataModel readRows(BufferedReader reader) throws IOException {
		DataModelBuilder builder = new DataModelBuilder();

		builder.addColumn(builder.createColumn("quantity", StringValue.class));
		builder.addColumn(builder.createColumn("measurement", StringValue.class));
		builder.addColumn(builder.createColumn("unit", StringValue.class));
		builder.addColumn(builder.createColumn("ignore", StringValue.class));
		builder.addColumn(builder.createColumn("date", StringValue.class));
		builder.addColumn(builder.createColumn("time", StringValue.class));


		String line;
		while ((line = reader.readLine()).indexOf("]") == -1) {
			String[] sections = line.split(",");
			Stream<DataValue> values = Arrays.stream(sections).map(e -> new StringValue(e));

			DataRow row = builder.createRow(values.toArray(size -> new DataValue[size]));

		}

		return builder.build();
	}
}
