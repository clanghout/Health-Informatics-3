package model.reader;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.DataRow;
import model.data.value.DataValue;
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
	private String name;

	/**
	 * Read the data from the file with the specified filename.
	 * @param filename The filename of the file you want to read the data from
	 * @return A DataTable containing the data from the specified file
	 * @throws IOException If anything goes wrong reading the file
	 */
	public DataTable readData(String filename) throws IOException {
		File file = new File(filename);
		name = filename;
		return readData(file);
	}

	/**
	 * Read the data from the specifed file.
	 * @param file The file you want to read the data from
	 * @return A DataTable containing the data from the specified file
	 * @throws IOException If anything goes wrong reading the file
	 */
	public DataTable readData(File file) throws IOException {
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
		return readData(stream);
	}

	/**
	 * Read the data from the specified stream.
	 * @param stream The stream you want to read the data from
	 * @return A DataTable containing the data from the specified stream
	 * @throws IOException If anything goes wrong reading the stream
	 */
	public DataTable readData(InputStream stream) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			skipToContent(reader);

			DataTableBuilder builder = new DataTableBuilder();
			builder.setName("test");
			addColumns(builder);

			DataTable table = readRows(reader, builder);

			return table;
		} catch (IOException e) {
			log.throwing(this.getClass().getSimpleName(), "readData", e);
			throw e;
		}
	}

	private void skipToContent(BufferedReader reader) throws IOException {
		while (!reader.readLine().contains("[")) {
			// Intentionally left empty
		}
	}

	private DataTable readRows(BufferedReader reader, DataTableBuilder builder) throws IOException {
		String line;
		while (!(line = reader.readLine()).contains("]")) {
			String[] sections = line.split(",");
			Stream<DataValue> values = Arrays.stream(sections).map(StringValue::new);

			builder.createRow(values.toArray(DataValue[]::new));
		}

		return builder.build();
	}

	private void addColumns(DataTableBuilder builder) {
		builder.createColumn("quantity", StringValue.class);
		builder.createColumn("measurement", StringValue.class);
		builder.createColumn("unit", StringValue.class);
		builder.createColumn("ignore", StringValue.class);
		builder.createColumn("date", StringValue.class);
		builder.createColumn("time", StringValue.class);
	}
}
