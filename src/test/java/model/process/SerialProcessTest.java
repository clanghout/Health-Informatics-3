package model.process;

import model.data.DataTable;
import model.data.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains the tests for the SerialProcess.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class SerialProcessTest {

	private DataProcess returnA;
	private DataProcess returnB;
	private DataProcess returnC;
	private DataTable a;
	private DataTable b;
	private DataTable c;

	@Before
	public void setUp() throws Exception {
		returnA = mock(DataProcess.class);
		returnB = mock(DataProcess.class);
		returnC = mock(DataProcess.class);

		a = new DataTable("A");
		b = new DataTable("B");
		c = new DataTable("C");

		when(returnA.doProcess()).thenReturn(a);
		when(returnB.doProcess()).thenReturn(b);
		when(returnC.doProcess()).thenReturn(c);

	}

	@Test
	public void testProcess() throws Exception {
		SerialProcess process = new SerialProcess(
				Arrays.asList(returnA, returnB, returnC)
		);

		Table result = process.process();

		assertEquals(returnA.getInput(), null);
		assertEquals(returnB.getInput(), a);
		assertEquals(returnC.getInput(), b);
		assertEquals(c, result);
	}
}