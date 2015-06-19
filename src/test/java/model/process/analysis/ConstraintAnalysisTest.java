package model.process.analysis;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.process.describer.ConstantDescriber;
import model.process.describer.ConstraintDescriber;
import model.process.describer.RowValueDescriber;
import model.process.analysis.operations.constraints.Constraint;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The test cases the The ConstraintsAnalysis
 * Created by Boudewijn on 5-5-2015.
 */
public class ConstraintAnalysisTest {

	@Test
	public void testAnalyse() throws Exception {
		DataTableBuilder builder =  new DataTableBuilder();
		builder.setName("test");

		DataColumn column = builder.createColumn("test", StringValue.class);

		DataRow pieRow = builder.createRow(new StringValue("Pie"));
		builder.createRow(new StringValue("is"));
		builder.createRow(new StringValue("nice"));

		DataTable input = builder.build();

		Constraint pieCheck = new EqualityCheck<>(
				new RowValueDescriber<>(column),
				new ConstantDescriber<>(new StringValue("Pie"))
		);

		ConstraintAnalysis analysis = new ConstraintAnalysis(new ConstraintDescriber(pieCheck));
		DataTable output = (DataTable) analysis.analyse(input);

		assertEquals(1, output.getRowCount());
		assertEquals(pieRow, output.getRow(0));
	}
}