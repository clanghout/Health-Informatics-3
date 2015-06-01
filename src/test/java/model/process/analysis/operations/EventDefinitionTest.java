package model.process.analysis.operations;

import model.data.*;
import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.FloatValue;
import model.data.value.StringValue;
import model.process.DataProcess;
import model.process.analysis.ConstraintAnalysis;
import model.process.analysis.operations.constraints.AndCheck;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.process.analysis.operations.constraints.NotCheck;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 5/28/15.
 */
public class EventDefinitionTest {

	@Test
	public void testSetCode() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");
		builder.createColumn("c1", StringValue.class);
		builder.createColumn("c2", FloatValue.class);
		builder.createRow(new StringValue("c2131"), new FloatValue(0));
		builder.createRow(new StringValue("affwa"), new FloatValue(3));
		builder.createRow(new StringValue("awf"), new FloatValue(1));
		builder.createRow(new StringValue("cawf1"), new FloatValue(0));

		DataTable table = builder.build();

		ConstraintAnalysis analyse = new ConstraintAnalysis(
				new ConstraintDescriber(new EqualityCheck<>(
						new ConstantDescriber(new FloatValue(0)),
						new RowValueDescriber<>(table.getColumn("c2")))));
		analyse.setInput(table.copy());
		EventDefinition event = new EventDefinition("code", analyse, table);

		event.setCode();

		assertTrue(table.getRow(0).containsCode("code"));
		assertFalse(table.getRow(2).containsCode("code"));
		assertTrue(table.getRow(3).containsCode("code"));

	}

	@Test
	public void testSetCode2() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("sensor");
		builder.createColumn("naam", StringValue.class);
		builder.createColumn("value", FloatValue.class);
		builder.createRow(new StringValue("a"), new FloatValue(0));
		builder.createRow(new StringValue("b"), new FloatValue(3));
		builder.createRow(new StringValue("c"), new FloatValue(1));
		builder.createRow(new StringValue("d"), new FloatValue(0));
		builder.createRow(new StringValue("e"), new FloatValue(0));

		DataTable sensor = builder.build();
		builder = new DataTableBuilder();
		builder.setName("website");
		builder.createColumn("naam", StringValue.class);
		builder.createColumn("value", FloatValue.class);
		builder.createRow(new StringValue("a"), new FloatValue(0));
		builder.createRow(new StringValue("b"), new FloatValue(1));
		builder.createRow(new StringValue("c"), new FloatValue(1));
		builder.createRow(new StringValue("d"), new FloatValue(2));
		builder.createRow(new StringValue("f"), new FloatValue(2));

		DataTable web = builder.build();

		ConstraintAnalysis analyse = new ConstraintAnalysis(
				new ConstraintDescriber(new AndCheck(
						new ConstraintDescriber(
								new EqualityCheck<>(
										new RowValueDescriber(sensor.getColumn("naam")),
										new RowValueDescriber(web.getColumn("naam"))
								)),
						new ConstraintDescriber(
								new NotCheck(
										new ConstraintDescriber(
												new EqualityCheck<>(
														new RowValueDescriber(web.getColumn("value")),
														new RowValueDescriber(sensor.getColumn("value"))))))
				)));

		analyse.setInput(new CombinedDataTable(sensor.export(sensor.getName()), web.export(web.getName())));
		EventDefinition event = new EventDefinition("code", analyse, sensor);

		event.setCode();

		assertFalse(sensor.getRow(0).containsCode("code"));
		assertTrue(sensor.getRow(1).containsCode("code"));
		assertFalse(sensor.getRow(2).containsCode("code"));
		assertTrue(sensor.getRow(3).containsCode("code"));
		assertFalse(sensor.getRow(4).containsCode("code"));

	}


}