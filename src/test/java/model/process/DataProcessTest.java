package model.process;

import model.data.DataTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * The test suite for DataProcess
 * <p/>
 * Created by Boudewijn on 5-5-2015.
 */
public class DataProcessTest {

	private DataProcess process;

	@Before
	public void setUp() throws Exception {
		process = mock(DataProcess.class);
	}

	@Test
	public void testProcess() throws Exception {
		DataTable output = new DataTable();

		when(process.doProcess()).thenReturn(output);

		process.process();
		verify(process).doProcess();
		assertEquals(output, process.getOutput());
	}

	@Test
	public void testSetInput() throws Exception {
		DataTable table = new DataTable();

		process.setInput(table);
		assertEquals(table, process.getInput());
	}
}