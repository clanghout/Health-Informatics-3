package model.input.file;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.value.*;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for the PlainTextFile class.
 * @author Paul
 *
 */
public class PlainTextFileTest {

	@Test
	public void testParseValues() throws Exception {
		PlainTextFile textFile;
		String file = getClass().getResource("/model/input/plaintext2.txt").getFile();
		textFile = new PlainTextFile(file);
		textFile.setDelimiter(",");
		textFile.setFirstRowAsHeader(true);

		textFile.addColumnInfo(new ColumnInfo(StringValue.class));
		textFile.addColumnInfo(new ColumnInfo(IntValue.class));
		textFile.addColumnInfo(new ColumnInfo(FloatValue.class));
		textFile.addColumnInfo(new ColumnInfo(DateTimeValue.class, "dd/MM/yy HH:mm:ss"));
		textFile.addColumnInfo(new ColumnInfo(DateValue.class, "dd/MM/yy"));
		textFile.addColumnInfo(new ColumnInfo(TimeValue.class, "HH:mm"));
		textFile.addColumnInfo(new ColumnInfo(StringValue.class));

		textFile.createMetaDataValue("MetaData", "string");
		textFile.setStartLine(6);
		textFile.setEndLine(2);

		DataTable table = textFile.createDataTable();
		DataRow row0 = table.getRow(0);
		assertEquals(new StringValue("Hallo"), row0.getValue(table.getColumn("Stringen")));
	}

	public void test() throws Exception {
		PlainTextFile textFile;
		String file = getClass().getResource("/model/input/plaintext.txt").getFile();
		textFile = new PlainTextFile(file);
		textFile.setDelimiter(" ");
		textFile.addColumnInfo(new ColumnInfo("test", StringValue.class));
		textFile.addColumnInfo(new ColumnInfo("int", IntValue.class));
		textFile.addColumnInfo(new ColumnInfo("float", FloatValue.class));
		textFile.addColumnInfo(new ColumnInfo("string", StringValue.class));

		DataTable table = textFile.createDataTable();
		List<DataColumn> columns = table.getColumns();

		assertEquals(StringValue.class, columns.get(0).getType());
		assertEquals(IntValue.class, columns.get(1).getType());
		assertEquals(FloatValue.class, columns.get(2).getType());
		assertEquals(StringValue.class, columns.get(3).getType());

		DataRow row = table.getRow(0);

		assertEquals(new StringValue("test"), row.getValue(table.getColumn("test")));
		assertEquals(new IntValue(5), row.getValue(table.getColumn("int")));
		assertEquals(new FloatValue(3.5f), row.getValue(table.getColumn("float")));
		assertEquals(new StringValue("dingen"), row.getValue(table.getColumn("string")));
	}
}
