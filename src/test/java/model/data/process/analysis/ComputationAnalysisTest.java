package model.data.process.analysis;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.ConstantDescriber;
import model.data.describer.RowValueDescriber;
import model.data.process.analysis.operations.computations.Addition;
import model.data.process.analysis.operations.computations.Computation;
import model.data.value.IntValue;
import model.data.value.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chris on 12-5-2015.
 */
public class ComputationAnalysisTest {

	@Test
	public void testAnalyse() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();

		DataColumn column = builder.createColumn("test", NumberValue.class);
		builder.addColumn(column);

		DataRow numRow = builder.createRow(new IntValue(123));
		builder.addRow(numRow);
		builder.addRow(builder.createRow(new IntValue(456)));
		builder.addRow(builder.createRow(new IntValue(247)));

		DataTable input = builder.build();

		Computation addCheck = new Addition(
				new RowValueDescriber<>(column),
				new ConstantDescriber<>(new IntValue(1))
		);

		ComputationAnalysis analysis = new ComputationAnalysis(addCheck);
		DataTable output = analysis.analyse(input);

		assertEquals(124, (int) output.getRow(0).getValue(column).getValue());
	}
}