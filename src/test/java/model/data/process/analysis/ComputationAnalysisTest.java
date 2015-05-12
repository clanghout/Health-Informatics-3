package model.data.process.analysis;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataModelBuilder;
import model.data.DataRow;
import model.data.describer.ConstantDescriber;
import model.data.describer.RowValueDescriber;
import model.data.process.analysis.computations.Addition;
import model.data.process.analysis.computations.Computation;
import model.data.value.IntValue;
import model.data.value.NumberValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 12-5-2015.
 */
public class ComputationAnalysisTest {

	@Test
	public void testAnalyse() throws Exception {
		DataModelBuilder builder = new DataModelBuilder();

		DataColumn column = builder.createColumn("test", NumberValue.class);
		builder.addColumn(column);

		DataRow numRow = builder.createRow(new IntValue(123));
		builder.addRow(numRow);
		builder.addRow(builder.createRow(new IntValue(456)));
		builder.addRow(builder.createRow(new IntValue(247)));

		DataModel input = builder.build();

		Computation addCheck = new Addition(
				new RowValueDescriber<>(column),
				new ConstantDescriber<>(new IntValue(1))
		);

		ComputationAnalysis analysis = new ComputationAnalysis(addCheck);
		DataModel output = analysis.analyse(input);

		assertEquals(124, output.getRow(0).getValue(column).getValue());
	}
}