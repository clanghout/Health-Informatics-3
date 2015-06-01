package model.process;

import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.FloatValue;
import model.data.value.StringValue;
import model.language.Identifier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/1/15.
 */
public class SetCodesTest {

	@Test
	public void testDoProcess() throws Exception {
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

		DataTable input = builder.build();
		DataTable codeTable = builder2.build();

		DataModel model = new DataModel();
		model.add(input);
		model.add(codeTable);

		SetCodes setCodes = new SetCodes("code", new Identifier<>("test2"));
		setCodes.setDataModel(model);
		setCodes.setInput(input);

		setCodes.process();
		DataTable output = (DataTable) setCodes.getOutput();

		assertFalse(output.getRow(0).containsCode("code"));
		assertTrue(output.getRow(1).containsCode("code"));
		assertTrue(output.getRow(2).containsCode("code"));
		assertFalse(output.getRow(3).containsCode("code"));
	}
}