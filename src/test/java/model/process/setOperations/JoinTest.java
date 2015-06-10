package model.process.setOperations;

import model.data.*;
import model.data.describer.OperationDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.FloatValue;
import model.data.value.StringValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.analysis.operations.constraints.EqualityCheck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/9/15.
 */
public class JoinTest {
	DataTable table1;
	DataTable table2;
	DataTable table3;
	DataModel model;
	ColumnIdentifier columnA;
	ColumnIdentifier columnB;
	ColumnIdentifier columnC;
	ColumnIdentifier columnD;
	ColumnIdentifier columnE;
	DataColumn colA;
	DataColumn colB;
	DataColumn colC;

	@Before
	public void setUp() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("c1", StringValue.class);
		colA = builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"));
		builder.createRow(new StringValue("cb23"), new StringValue("ca33"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"));
		builder.createRow(new StringValue("cb13"), new StringValue("ca23"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"));
		columnA = new ColumnIdentifier(new Identifier("test1"), new Identifier("c2"));
		table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("c1", StringValue.class);
		colB = builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("ac14"), new StringValue("d24"));
		builder.createRow(new StringValue("ac13"), new StringValue("c24"));
		builder.createRow(new StringValue("ac15"), new StringValue("d25"));
		builder.createRow(new StringValue("ac16"), new StringValue("d26"));
		columnB = new ColumnIdentifier(new Identifier("test2"), new Identifier("c2"));
		table2 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test3");
		builder.createColumn("c1", StringValue.class);
		colC = builder.createColumn("c2", StringValue.class);
		builder.createColumn("c3", StringValue.class);
		builder.createColumn("c4", StringValue.class);
		builder.createRow(new StringValue("k11"), new StringValue("c21"),
				new StringValue("c21"), new StringValue("c21"));
		builder.createRow(new StringValue("kcb23"), new StringValue("cadf33"),
				new StringValue("cadf33"), new StringValue("cadf33"));
		builder.createRow(new StringValue("k12"), new StringValue("c22"),
				new StringValue("c22"), new StringValue("c22"));
		builder.createRow(new StringValue("k13"), new StringValue("c23"),
				new StringValue("c23"), new StringValue("c23"));
		table3 = builder.build();
		columnC = new ColumnIdentifier(new Identifier("test3"), new Identifier("c2"));
		columnD = new ColumnIdentifier(new Identifier("test3"), new Identifier("c3"));
		columnE = new ColumnIdentifier(new Identifier("test3"), new Identifier("c4"));


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
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

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
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testAddCombineDubbleColumn() throws Exception {
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
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

		join.addCombineColumn(columnB, columnA);
		join.addCombineColumn(columnA, columnB);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testAddMultipleCombineColumn() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test3_c1", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"), new StringValue("k11"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("k12"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("k13"));
		DataTable expected = builder.build();


		SimpleJoin join = new SimpleJoin("res", new CombinedDataTable(table1, table3));
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colC))));

		join.addCombineColumn(columnC, columnA);
		join.addCombineColumn(columnD, columnA);
		join.addCombineColumn(columnE, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testAddMultipleCombineColumn2() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test3_c1", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"), new StringValue("k11"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("k12"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("k13"));
		DataTable expected = builder.build();


		SimpleJoin join = new SimpleJoin("res", new CombinedDataTable(table1, table3));
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colC))));

		join.addCombineColumn(columnC, columnA);
		join.addCombineColumn(columnD, columnC);
		join.addCombineColumn(columnE, columnD);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddCombineColumnWrongType() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		DataColumn c = builder.createColumn("c2", FloatValue.class);
		DataTable t = builder.build();


		SimpleJoin join = new SimpleJoin("res", new CombinedDataTable(table1, t));
		join.addCombineColumn(
				new ColumnIdentifier(new Identifier("res"), new Identifier("c2"))
				, columnA);

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

		FullJoin join = new FullJoin("res", table1, table2, FullJoin.Join.JOIN);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

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


		FullJoin join = new FullJoin("res", table1, table2, FullJoin.Join.JOIN);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}

	@Test (expected = IllegalStateException.class)
	public void testBuildFJoin() throws Exception {
		FullJoin join = new FullJoin("res", table1, table2, FullJoin.Join.JOIN);
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
				FullJoin.Join.LEFT);
		join.setDataModel(model);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

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
				FullJoin.Join.RIGHT);
		join.setDataModel(model);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

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
				FullJoin.Join.FULL);
		join.setDataModel(model);
		join.setConstraint(new OperationDescriber<>(
				new EqualityCheck<>(new RowValueDescriber<>(colA), new RowValueDescriber(colB))));

		join.addCombineColumn(columnB, columnA);
		DataTable res = (DataTable) join.process();

		assertTrue(res.equalsSoft(expected));
	}


}