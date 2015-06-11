package model.process.analysis.operations;

import model.data.CombinedDataTable;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.ComputationDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.StringValue;
import model.data.value.TimeValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.analysis.operations.computations.Addition;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/11/15.
 */
public class ColumnComputationTest {
	DataTable table1;
	DataTable table2;
	DataModel model;

	@Before
	public void setup() {
		model = new DataModel();
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("table1");
		builder.createColumn("int", IntValue.class);
		builder.createColumn("value", StringValue.class);
		builder.createColumn("intneg", IntValue.class);
		builder.createRow(new IntValue(2), new StringValue("1"), new IntValue(-2));
		builder.createRow(new IntValue(5), new StringValue("5"), new IntValue(-5));
		builder.createRow(new IntValue(4), new StringValue("3"), new IntValue(-4));
		builder.createRow(new IntValue(6), new StringValue("6"), new IntValue(-6));
		table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("table2");
		builder.createColumn("int", IntValue.class);
		builder.createColumn("value2", StringValue.class);
		builder.createRow(new IntValue(4), new StringValue("4"));
		builder.createRow(new IntValue(2), new StringValue("2"));
		builder.createRow(new IntValue(7), new StringValue("7"));
		table2 = builder.build();

		model.add(table1);
		model.add(table2);
	}

	@Test
	public void testDoProcess() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("int2", IntValue.class);
		builder.createColumn("value", StringValue.class);
		builder.createRow(new IntValue(2), new StringValue("1"));
		builder.createRow(new IntValue(5), new StringValue("5"));
		builder.createRow(new IntValue(4), new StringValue("3"));
		builder.createRow(new IntValue(6), new StringValue("6"));

		DataTable expected = builder.build();

		ColumnComputation col = new ColumnComputation("res");
		col.addColumn(
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table1"), new Identifier("int"))),
				"int2");
		col.addColumn(
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table1"), new Identifier("value"))),
				"value");

		col.setDataModel(model);
		col.setInput(table1);
		DataTable res = (DataTable) col.process();

		assertTrue(expected.equalsSoft(res));

	}

	@Test
	public void testDoProcessCombinedDataTable() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("int2", IntValue.class);
		builder.createColumn("value", StringValue.class);
		builder.createRow(new IntValue(2), new StringValue("4"));
		builder.createRow(new IntValue(5), new StringValue("4"));
		builder.createRow(new IntValue(4), new StringValue("4"));
		builder.createRow(new IntValue(6), new StringValue("4"));
		builder.createRow(new IntValue(2), new StringValue("2"));
		builder.createRow(new IntValue(5), new StringValue("2"));
		builder.createRow(new IntValue(4), new StringValue("2"));
		builder.createRow(new IntValue(6), new StringValue("2"));
		builder.createRow(new IntValue(2), new StringValue("7"));
		builder.createRow(new IntValue(5), new StringValue("7"));
		builder.createRow(new IntValue(4), new StringValue("7"));
		builder.createRow(new IntValue(6), new StringValue("7"));

		DataTable expected = builder.build();

		ColumnComputation col = new ColumnComputation("res");
		col.addColumn(
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table1"), new Identifier("int"))),
				"int2");
		col.addColumn(
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table2"), new Identifier("value2"))),
				"value");

		col.setDataModel(model);
		col.setInput(new CombinedDataTable(table1, table2));
		DataTable res = (DataTable) col.process();

		assertTrue(expected.equalsSoft(res));

	}

	@Test
	public void testDoProcessComputation() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("int2", IntValue.class);
		builder.createColumn("value", StringValue.class);
		builder.createColumn("sum", IntValue.class);
		builder.createRow(new IntValue(2), new StringValue("4"), new IntValue(6));
		builder.createRow(new IntValue(5), new StringValue("4"), new IntValue(9));
		builder.createRow(new IntValue(4), new StringValue("4"), new IntValue(8));
		builder.createRow(new IntValue(6), new StringValue("4"), new IntValue(10));
		builder.createRow(new IntValue(2), new StringValue("2"), new IntValue(4));
		builder.createRow(new IntValue(5), new StringValue("2"), new IntValue(7));
		builder.createRow(new IntValue(4), new StringValue("2"), new IntValue(6));
		builder.createRow(new IntValue(6), new StringValue("2"), new IntValue(8));
		builder.createRow(new IntValue(2), new StringValue("7"), new IntValue(9));
		builder.createRow(new IntValue(5), new StringValue("7"), new IntValue(12));
		builder.createRow(new IntValue(4), new StringValue("7"), new IntValue(11));
		builder.createRow(new IntValue(6), new StringValue("7"), new IntValue(13));

		DataTable expected = builder.build();

		ColumnComputation col = new ColumnComputation("res");
		col.addColumn(
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table1"), new Identifier("int"))),
				"int2");
		col.addColumn(
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table2"), new Identifier("value2"))),
				"value");

		col.addColumn(new ComputationDescriber<>(
				new Addition(
						new TableValueDescriber<>(model,
								new ColumnIdentifier(new Identifier("table1"), new Identifier("int"))),
				new TableValueDescriber<>(model,
						new ColumnIdentifier(new Identifier("table2"), new Identifier("int"))))), "sum");

		col.setDataModel(model);
		col.setInput(new CombinedDataTable(table1, table2));
		DataTable res = (DataTable) col.process();

		assertTrue(expected.equalsSoft(res));

	}

}