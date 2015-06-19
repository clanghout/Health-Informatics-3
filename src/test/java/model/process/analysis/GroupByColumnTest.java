package model.process.analysis;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.process.describer.RowValueDescriber;
import model.data.value.FloatValue;
import model.data.value.StringValue;
import model.process.functions.Function;
import model.process.functions.Maximum;
import model.process.functions.Minimum;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/3/15.
 */
public class GroupByColumnTest {
	DataTable table;
	DataColumn c1;
	DataColumn c2;

	@Before
	public void setup() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		c1 = builder.createColumn("c1", StringValue.class);
		c2 = builder.createColumn("c2", FloatValue.class);
		builder.createRow(new StringValue("test"), new FloatValue(2f));
		builder.createRow(new StringValue("bob"), new FloatValue(3f));
		builder.createRow(new StringValue("henk"), new FloatValue(4f));
		builder.createRow(new StringValue("test"), new FloatValue(5f));

		builder.createRow(new StringValue("test"), new FloatValue(2f));
		builder.createRow(new StringValue("henk"), new FloatValue(-1f));
		builder.createRow(new StringValue("test"), new FloatValue(3f));
		builder.createRow(new StringValue("henk"), new FloatValue(5f));

		table = builder.build();
	}


	@Test
	public void testAnalyseNoFuncions() throws Exception {
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				new ArrayList<Function>(), new ArrayList<String>());
		groupBy.setDataModel(new DataModel());
		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(out.getRows().size(), 3);
		assertEquals(out.getName(), "test2");
		assertEquals("test", out.getRow(0).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("bob", out.getRow(1).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("henk", out.getRow(2).getValue(out.getColumn("Chunk")).getValue());
	}

	@Test
	public void testAnalyseFunction() throws Exception {
		List<Function> functions = new ArrayList<>();
		List<String> name = new ArrayList<>();

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<>(c2)));
		name.add("max");
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);
		groupBy.setDataModel(new DataModel());
		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(out.getRows().size(), 3);
		assertEquals(out.getName(), "test2");
		assertEquals("test", out.getRow(0).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("bob", out.getRow(1).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("henk", out.getRow(2).getValue(out.getColumn("Chunk")).getValue());

		assertEquals(new FloatValue(5f), out.getRow(0).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(3f), out.getRow(1).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(5f), out.getRow(2).getValue(out.getColumn("max")));

	}

	@Test
	public void testAnalyseMultipleFunction() throws Exception {
		List<Function> functions = new ArrayList<>();
		List<String> name = new ArrayList<>();

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<>(c2)));
		name.add("max");
		functions.add(new Minimum(new DataTable(), new RowValueDescriber<>(c2)));
		name.add("min");
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);
		groupBy.setDataModel(new DataModel());
		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(out.getRows().size(), 3);
		assertEquals(out.getName(), "test2");
		assertEquals("test", out.getRow(0).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("bob", out.getRow(1).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("henk", out.getRow(2).getValue(out.getColumn("Chunk")).getValue());

		assertEquals(new FloatValue(5f), out.getRow(0).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(3f), out.getRow(1).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(5f), out.getRow(2).getValue(out.getColumn("max")));

		assertEquals(new FloatValue(2f), out.getRow(0).getValue(out.getColumn("min")));
		assertEquals(new FloatValue(3f), out.getRow(1).getValue(out.getColumn("min")));
		assertEquals(new FloatValue(-1f), out.getRow(2).getValue(out.getColumn("min")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnbalancedArguments() throws Exception {
		List<Function> functions = new ArrayList<>();
		List<String> name = new ArrayList<>();

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<>(c2)));
		name.add("max");
		functions.add(new Minimum(new DataTable(), new RowValueDescriber<>(c2)));
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);

		DataTable out = (DataTable) groupBy.analyse(table);

	}

	@Test
	public void testEmptyTable() throws Exception {
		List<Function> functions = new ArrayList<>();
		List<String> name = new ArrayList<>();

		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		c1 = builder.createColumn("c1", StringValue.class);
		c2 = builder.createColumn("c2", FloatValue.class);

		DataTable table = builder.build();

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<>(c2)));
		name.add("max");
		functions.add(new Minimum(new DataTable(), new RowValueDescriber<>(c2)));
		name.add("min");
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);

		groupBy.setDataModel(new DataModel());
		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(0, out.getRowCount());

	}

}