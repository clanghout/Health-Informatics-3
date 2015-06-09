package model.data;

import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.StringValue;
import model.process.analysis.operations.constraints.EqualityCheck;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/9/15.
 */
public class DataTableJoinBuilderTest {

	@Test
	public void testSetConstraint() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("c1", StringValue.class);
		DataColumn a = builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"));
		DataTable table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("c1", StringValue.class);
		DataColumn b =builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("ac13"), new StringValue("c24"));
		DataTable table2 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("test1_c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createColumn("test2_c2", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"), new StringValue("c24"));
		DataTable expected = builder.build();


		DataTableJoinBuilder join = new DataTableJoinBuilder(table1, table2, false, false);
		join.setConstraint(new ConstraintDescriber(
				new EqualityCheck<>(new RowValueDescriber<>(a), new RowValueDescriber(b))));

		join.setName("res");
		DataTable res = join.build();

		assertTrue(res.equalsSoft(expected));



	}

	@Test
	public void testAddCombineColumn() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("c1", StringValue.class);
		DataColumn a = builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"));
		DataTable table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("c1", StringValue.class);
		DataColumn b =builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("ac13"), new StringValue("c24"));
		DataTable table2 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("test1_c1", StringValue.class);
		builder.createColumn("c2", StringValue.class);
		builder.createColumn("test2_c1", StringValue.class);
		builder.createRow(new StringValue("c12"), new StringValue("c22"), new StringValue("ac12"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"), new StringValue("ac11"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"), new StringValue("ac13"));
		DataTable expected = builder.build();


		DataTableJoinBuilder join = new DataTableJoinBuilder(table1, table2, false, false);
		join.setConstraint(new ConstraintDescriber(
				new EqualityCheck<>(new RowValueDescriber<>(a), new RowValueDescriber(b))));

		join.setName("res");
		join.addCombineColumn(b, a);
		DataTable res = join.build();

		assertTrue(res.equalsSoft(expected));
	}

	@Test
	public void testBuild() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test1");
		builder.createColumn("c1", StringValue.class);
		DataColumn a = builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("c11"), new StringValue("c21"));
		builder.createRow(new StringValue("c12"), new StringValue("c22"));
		builder.createRow(new StringValue("c13"), new StringValue("c23"));
		builder.createRow(new StringValue("c14"), new StringValue("c24"));
		DataTable table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("test2");
		builder.createColumn("c1", StringValue.class);
		DataColumn b =builder.createColumn("c2", StringValue.class);
		builder.createRow(new StringValue("ac11"), new StringValue("c23"));
		builder.createRow(new StringValue("ac12"), new StringValue("c22"));
		builder.createRow(new StringValue("ac13"), new StringValue("c24"));
		DataTable table2 = builder.build();


		DataTableJoinBuilder join = new DataTableJoinBuilder(table1, table2, false, false);
		join.setName("res");
		join.addCombineColumn(b, a);
		DataTable res = join.build();

	}
}