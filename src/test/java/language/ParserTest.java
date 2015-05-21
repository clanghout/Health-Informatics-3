package language;

import model.data.DataModel;
import model.data.DataTable;
import model.data.Table;
import model.data.process.DataProcess;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The tests for the Parser.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class ParserTest {

	@Test
	public void testParse() throws Exception {
		String input = "from(test1)";

		DataModel model = new DataModel();
		DataTable test1 = new DataTable("test1");
		model.add(test1);

		Parser parser = new Parser();
		DataProcess process = parser.parse(input, model);

		process.setDataModel(model);
		Table result = process.process();

		assertEquals(test1, result);
	}
}