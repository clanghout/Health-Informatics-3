package model.process.analysis;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.NumberValue;
import model.data.value.StringValue;
import model.process.analysis.operations.constraints.EqualityCheck;
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
		builder.createRow(new StringValue("test"), new FloatValue(2));
		builder.createRow(new StringValue("bob"), new FloatValue(3));
		builder.createRow(new StringValue("henk"), new FloatValue(4));
		builder.createRow(new StringValue("test"), new FloatValue(5));

		builder.createRow(new StringValue("test"), new FloatValue(2));
		builder.createRow(new StringValue("henk"), new FloatValue(-1));
		builder.createRow(new StringValue("test"), new FloatValue(3));
		builder.createRow(new StringValue("henk"), new FloatValue(5));

		try {
			table = builder.build();
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception");
		}
	}


	@Test
	public void testAnalyseNoFuncions() throws Exception {
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				new ArrayList<Function>(), new ArrayList<String>());

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

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
		name.add("max");
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);

		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(out.getRows().size(), 3);
		assertEquals(out.getName(), "test2");
		assertEquals("test", out.getRow(0).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("bob", out.getRow(1).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("henk", out.getRow(2).getValue(out.getColumn("Chunk")).getValue());

		assertEquals(new FloatValue(5), out.getRow(0).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(3), out.getRow(1).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(5), out.getRow(2).getValue(out.getColumn("max")));

	}

	@Test
	public void testAnalyseMultipleFunction() throws Exception {
		List<Function> functions = new ArrayList<>();
		List<String> name = new ArrayList<>();

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
		name.add("max");
		functions.add(new Minimum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
		name.add("min");
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);

		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(out.getRows().size(), 3);
		assertEquals(out.getName(), "test2");
		assertEquals("test", out.getRow(0).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("bob", out.getRow(1).getValue(out.getColumn("Chunk")).getValue());
		assertEquals("henk", out.getRow(2).getValue(out.getColumn("Chunk")).getValue());

		assertEquals(new FloatValue(5), out.getRow(0).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(3), out.getRow(1).getValue(out.getColumn("max")));
		assertEquals(new FloatValue(5), out.getRow(2).getValue(out.getColumn("max")));

		assertEquals(new FloatValue(2), out.getRow(0).getValue(out.getColumn("min")));
		assertEquals(new FloatValue(3), out.getRow(1).getValue(out.getColumn("min")));
		assertEquals(new FloatValue(-1), out.getRow(2).getValue(out.getColumn("min")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnbalancedArguments() throws Exception {
		List<Function> functions = new ArrayList<>();
		List<String> name = new ArrayList<>();

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
		name.add("max");
		functions.add(new Minimum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
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

		functions.add(new Maximum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
		name.add("max");
		functions.add(new Minimum(new DataTable(), new RowValueDescriber<NumberValue>(c2)));
		name.add("min");
		GroupByColumn groupBy = new GroupByColumn("test2", new RowValueDescriber<>(c1),
				functions, name);

		DataTable out = (DataTable) groupBy.analyse(table);

		assertEquals(0, out.getRowCount());

	}

}