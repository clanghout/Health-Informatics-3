package model.process;

import model.data.DataModel;
import model.data.DataTable;
import model.language.Identifier;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by Boudewijn on 2-6-2015.
 */
public class FromProcessTest {

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyIdentifiers() throws Exception {
		FromProcess process = new FromProcess();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTableIdentifier() throws Exception {
		FromProcess process = new FromProcess(new Identifier<DataTable>("hello"));

		DataModel model = new DataModel();

		process.setDataModel(model);

		process.doProcess();
	}
}