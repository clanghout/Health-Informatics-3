package model.data;

import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.OperationDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.StringValue;
import model.input.file.DataFile;
import model.language.Identifier;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.process.setOperations.FullJoin;
import model.process.setOperations.SimpleJoin;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/9/15.
 */
public class JoinTest {
	DataTable table1;
	DataTable table2;
	DataModel model;
	DataColumn columnA;
	DataColumn columnB;

	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("c1", StringValue.class);
		columnA = builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"));
		builder.createRow(new StringValue("cb23"), new StringValue("ca33"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"));
		builder.createRow(new StringValue("cb13"), new StringValue("ca23"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"));
		table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("c1", StringValue.class);
		columnB =builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("ac14"), new StringValue("d24"));
		builder.createRow(new StringValue("ac13"), new StringValue("c24"));
		builder.createRow(new StringValue("ac15"), new StringValue("d25"));
		builder.createRow(new StringValue("ac16"), new StringValue("d26"));

		table2 = builder.build();

		model = new DataModel();
		model.add(table1);
		model.add(table2);
	}

	@Test
	public void testSetConstraint() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("test1_c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createColumn("test2_c2", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"), new StringValue("c24"));
		DataTable expected = builder.build();

		SimpleJoin join = new SimpleJoin("res", new CombinedDataTable(table1, table2));
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	 public void testAddCombineColumn() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"));
		DataTable expected = builder.build();


		SimpleJoin join = new SimpleJoin("res", new CombinedDataTable(table1, table2));
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testJoinOneTable() throws Exception {

		SimpleJoin join = new SimpleJoin("res", new Identifier<>("test1"));
		join.setDataModel(model);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(table1));
	}

	@Test (expected = IllegalStateException.class)
	public void testBuild() throws Exception {
		SimpleJoin join = new SimpleJoin("res", new CombinedDataTable(table1, table2));
		join.addCombineColumn(columnB, columnA);
		join.process();
	}

	@Test
	public void testSetConstraintFJoin() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("test1_c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createColumn("test2_c2", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"), new StringValue("c24"));
		DataTable expected = builder.build();

		FullJoin join = new FullJoin("res", table1, table2, false, false);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testAddCombineColumnFJoin() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"));
		DataTable expected = builder.build();


		FullJoin join = new FullJoin("res", table1, table2, false, false);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test (expected = IllegalStateException.class)
	public void testBuildFJoin() throws Exception {
		FullJoin join = new FullJoin("res", table1, table2, false, false);
		join.addCombineColumn(columnB, columnA);
		join.process();
	}

	@Test
	public void testAddCombineColumnFullLeftJoin() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"), new StringValue(null));
		builder.createRow(new StringValue("cb23"), new StringValue("ca33"), new StringValue(null));
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"));
		builder.createRow(new StringValue("cb13"), new StringValue("ca23"), new StringValue(null));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"));
		DataTable expected = builder.build();


		FullJoin join = new FullJoin("res", new Identifier<>("test1"), new Identifier<>("test2"),
				true, false);
		join.setDataModel(model);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testAddCombineColumnFullRightJoin() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"));
		builder.createRow(new StringValue(null), new StringValue("d24"), new StringValue("ac14"));
		builder.createRow(new StringValue(null), new StringValue("d25"), new StringValue("ac15"));
		builder.createRow(new StringValue(null), new StringValue("d26"), new StringValue("ac16"));


		DataTable expected = builder.build();


		FullJoin join = new FullJoin("res", new Identifier<>("test1"), new Identifier<>("test2"),
				false, true);
		join.setDataModel(model);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testAddCombineColumnFullJoin() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"), new StringValue(null));
		builder.createRow(new StringValue("cb23"), new StringValue("ca33"), new StringValue(null));
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"));
		builder.createRow(new StringValue("cb13"), new StringValue("ca23"), new StringValue(null));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"));
		builder.createRow(new StringValue(null), new StringValue("d24"), new StringValue("ac14"));
		builder.createRow(new StringValue(null), new StringValue("d25"), new StringValue("ac15"));
		builder.createRow(new StringValue(null), new StringValue("d26"), new StringValue("ac16"));


		DataTable expected = builder.build();


		FullJoin join = new FullJoin("res", new Identifier<>("test1"), new Identifier<>("test2"),
				true, true);
		join.setDataModel(model);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(columnA), new RowValueDescriber(columnB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}


}