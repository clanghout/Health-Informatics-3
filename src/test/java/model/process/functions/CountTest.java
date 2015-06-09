package model.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.RowValueDescriber;
import model.data.value.IntValue;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/4/15.
 */
public class CountTest {

	@Test
	public void emptyCountTest() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		DataColumn c1 = builder.createColumn("c1", StringValue.class);
		DataTable table;
		try {
			table = builder.build();
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception");
		}

		Count count = new Count(table, null);

		assertEquals(new IntValue(0), count.calculate());
	}

	@Test
	public void countTest() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		DataColumn c1 = builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("t1"));
		DataTable table;
		try {
			table = builder.build();
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception");
		}

		Count count = new Count(table, null);

		assertEquals(new IntValue(1), count.calculate());
	}

	@Test
	public void countMultipleRowsTest() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		DataColumn c1 = builder.createColumn("c1", StringValue.class);
		builder.createRow(new StringValue("t1"));
		builder.createRow(new StringValue("t2"));
		builder.createRow(new StringValue("t3"));
		DataTable table;

		try {
			table = builder.build();
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception");
		}

		Count count = new Count(table, new RowValueDescriber<>(c1));

		assertEquals(new IntValue(3), count.calculate());
	}

}