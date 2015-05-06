package model.data.process.analysis;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataModelBuilder;
import model.data.DataRow;
import model.data.process.analysis.constraints.Constraint;
import model.data.process.analysis.constraints.EqualityCheck;
import model.data.value.StringValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Boudewijn on 5-5-2015.
 */
public class ConstraintAnalysisTest {

	@Test
	public void testAnalyse() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();

		DataColumn column = builder.createColumn("test", StringValue.class);
		builder.addColumn(column);

		DataRow pieRow = builder.createRow(new StringValue("Pie"));
		builder.addRow(pieRow);
		builder.addRow(builder.createRow(new StringValue("is")));
		builder.addRow(builder.createRow(new StringValue("nice")));

		DataModel input = builder.build();

		Constraint pieCheck = new EqualityCheck(column, new StringValue("Pie"));

		ConstraintAnalysis analysis = new ConstraintAnalysis(pieCheck);
		DataModel output = analysis.analyse(input);

		assertEquals(1, output.getRowCount());
		assertEquals(pieRow, output.getRow(0));
	}
}