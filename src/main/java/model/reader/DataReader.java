package model.reader;

import model.data.DataModel;
import model.data.DataModelBuilder;
import model.data.DataRow;
import model.data.DataValue;
import model.data.value.StringValue;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * This class can be used to read the data from the StatSenser
 *
 * Created by Boudewijn on 29-4-2015.
 */
public class DataReader {

	private Logger log = Logger.getLogger("DataReader");

	/**
	 * Read the data from the file with the specified filename.
	 * @param filename The filename of the file you want to read the data from
	 * @return A DataModel containing the data from the specified file
	 * @throws IOException If anything goes wrong reading the file
	 */
	public DataModel readData(String filename) throws IOException {
		File file = new File(filename);
		return readData(file);
	}

	/**
	 * Read the data from the specifed file
	 * @param file The file you want to read the data from
	 * @return A DataModel containing the data from the specified file
	 * @throws IOException If anything goes wrong reading the file
	 */
	public DataModel readData(File file) throws IOException {
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
		return readData(stream);
	}

	/**
	 * Read the data from the specified stream
	 * @param stream The stream you want to read the data from
	 * @return A DataModel containing the data from the specified stream
	 * @throws IOException If anything goes wrong reading the stream
	 */
	public DataModel readData(InputStream stream) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			skipToContent(reader);

			DataModelBuilder builder = new DataModelBuilder();
			addColumns(builder);

			DataModel model = readRows(reader, builder);

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

	private DataModel readRows(BufferedReader reader, DataModelBuilder builder) throws IOException {
		String line;
		while ((line = reader.readLine()).indexOf("]") == -1) {
			String[] sections = line.split(",");
			Stream<DataValue> values = Arrays.stream(sections).map(e -> new StringValue(e));

			DataRow row = builder.createRow(values.toArray(size -> new DataValue[size]));
			builder.addRow(row);
		}

		return builder.build();
	}

	private void addColumns(DataModelBuilder builder) {
		builder.addColumn(builder.createColumn("quantity", StringValue.class));
		builder.addColumn(builder.createColumn("measurement", StringValue.class));
		builder.addColumn(builder.createColumn("unit", StringValue.class));
		builder.addColumn(builder.createColumn("ignore", StringValue.class));
		builder.addColumn(builder.createColumn("date", StringValue.class));
		builder.addColumn(builder.createColumn("time", StringValue.class));
	}
}
