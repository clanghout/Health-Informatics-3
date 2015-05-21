package language;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains the tests for ProcessInfo.
 *
 * (This is rather trivial.)
 * Created by Boudewijn on 20-5-2015.
 */
public class ProcessInfoTest {

	private Identifier<Object> identifier;
	private Object[] parameters;
	private ProcessInfo info;

	@Before
	public void setUp() throws Exception {
		identifier = new Identifier<>("test");
		parameters = new Object[] {
				5, 1.0
		};
		info = new ProcessInfo(identifier, parameters);
	}

	@Test
	public void testGetParameters() throws Exception {
		assertArrayEquals(parameters, info.getParameters());
	}

	@Test
	public void testGetName() throws Exception {
		assertEquals(identifier, info.getIdentifier());
	}
}