package model.input.file;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.value.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		DataRow row1 = table.getRow(1);
		DataRow row2 = table.getRow(2);
		DataRow row3 = table.getRow(3);
		DataRow row4 = table.getRow(4);

		assertEquals(new StringValue("Hallo"), row0.getValue(table.getColumn("Stringen")));
		assertTrue(row2.getValue(table.getColumn("Stringen")).isNull());
		assertEquals(new StringValue("textfile"), row4.getValue(table.getColumn("Stringen")));

		assertEquals(new IntValue(10), row0.getValue(table.getColumn("Integers")));
		assertTrue(row2.getValue(table.getColumn("Integers")).isNull());
		assertEquals(new IntValue(20), row4.getValue(table.getColumn("Integers")));

		assertEquals(new FloatValue(-25.5f), row0.getValue(table.getColumn("Floaten")));
		assertTrue(row1.getValue(table.getColumn("Floaten")).isNull());
		assertEquals(new FloatValue(13.8f), row2.getValue(table.getColumn("Floaten")));
		assertEquals(new FloatValue(12.0f), row4.getValue(table.getColumn("Floaten")));

		assertEquals(new DateTimeValue(2008, 8, 8, 8, 8, 8), row0.getValue(table.getColumn("Datumtijden")));
		assertTrue(row2.getValue(table.getColumn("Datumtijden")).isNull());
		assertEquals(new DateTimeValue(2010, 9, 8, 5, 8, 25), row3.getValue(table.getColumn("Datumtijden")));
		assertTrue(row4.getValue(table.getColumn("Datumtijden")).isNull());


		assertTrue(row0.getValue(table.getColumn("Datums(dd/mm/yy)")).isNull());
		assertEquals(new DateValue(2012, 12, 12), row1.getValue(table.getColumn("Datums(dd/mm/yy)")));
		assertEquals(new DateValue(2013, 9, 5), row3.getValue(table.getColumn("Datums(dd/mm/yy)")));

		assertEquals(new TimeValue(8, 0, 0), row1.getValue(table.getColumn("Tijden(hh:mm)")));
		assertTrue(row2.getValue(table.getColumn("Tijden(hh:mm)")).isNull());
		assertEquals(new TimeValue(23, 59, 0), row3.getValue(table.getColumn("Tijden(hh:mm)")));

		assertEquals(new StringValue("plaintext2"), row3.getValue(table.getColumn("MetaData")));
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
