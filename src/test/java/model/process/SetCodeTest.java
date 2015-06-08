package model.process;

import model.data.CombinedDataTable;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.describer.ConstantDescriber;
import model.data.value.FloatValue;
import model.data.value.StringValue;
import model.language.Identifier;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/1/15.
 */
public class SetCodeTest {
	DataTable input;
	DataTable codeTable;
	DataTable table3;

	@Before
	public void setup() {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("test");

		builder.createColumn("c1", StringValue.class);
		builder.createColumn("c2", FloatValue.class);

		builder.createRow(new StringValue("test"), new FloatValue(3));
		builder.createRow(new StringValue("test2"), new FloatValue(2));
		builder.createRow(new StringValue("test3"), new FloatValue(1));
		builder.createRow(new StringValue("test4"), new FloatValue(0));

		DataTableBuilder builder2 = new DataTableBuilder();
		builder2.setName("test2");

		builder2.createColumn("c1", StringValue.class);
		builder2.createColumn("c2", FloatValue.class);

		builder2.createRow(new StringValue("test2"), new FloatValue(2));
		builder2.createRow(new StringValue("test3"), new FloatValue(1));

		input = builder.build();
		codeTable = builder2.build();

		builder = new DataTableBuilder();
		builder.setName("test3");

		builder.createColumn("c21", StringValue.class);

		builder.createRow(new StringValue("test"));
		builder.createRow(new StringValue("test2"));

		table3 = builder.build();

	}

	@Test
	public void testDoProcess() throws Exception {
		DataModel model = new DataModel();
		model.add(input);
		model.add(codeTable);

		SetCode setCodes = new SetCode(
				new ConstantDescriber<>(new StringValue("code")),
				new Identifier<>("test2")
		);
		setCodes.setDataModel(model);
		setCodes.setInput(input);

		setCodes.process();
		DataTable output = (DataTable) setCodes.getOutput();

		assertFalse(output.getRow(0).containsCode("code"));
		assertTrue(output.getRow(1).containsCode("code"));
		assertTrue(output.getRow(2).containsCode("code"));
		assertFalse(output.getRow(3).containsCode("code"));
	}

	@Test (expected = IllegalStateException.class)
	public void testSetCodeTableNotSet() throws Exception {
		DataModel model = new DataModel();
		model.add(input);
		model.add(codeTable);

		SetCode setCodes = new SetCode(
				new ConstantDescriber<>(new StringValue("code")),
				new Identifier<>("test2")
		);
		setCodes.setDataModel(model);

		setCodes.process();
	}

	@Test
	public void testSetCodeCombInputTable() throws Exception {
		DataModel model = new DataModel();
		model.add(input);
		model.add(codeTable);
		model.add(table3);

		CombinedDataTable comb = new CombinedDataTable(table3, input);

		SetCode setCodes = new SetCode(
				new ConstantDescriber<>(new StringValue("code")),
				new Identifier<>("test2")
		);
		setCodes.setDataModel(model);
		setCodes.setInput(comb);

		setCodes.process();
		CombinedDataTable output = (CombinedDataTable) setCodes.getOutput();

		assertFalse(output.getTables().get(1).getRow(0).containsCode("code"));
		assertTrue(output.getTables().get(1).getRow(1).containsCode("code"));
		assertTrue(output.getTables().get(1).getRow(2).containsCode("code"));
		assertFalse(output.getTables().get(1).getRow(3).containsCode("code"));

		assertFalse(output.getTables().get(0).getRow(0).containsCode("code"));
		assertFalse(output.getTables().get(0).getRow(1).containsCode("code"));
	}

}