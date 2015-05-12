package model.data;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by jens on 5/12/15.
 */
public class DataConnectionTest {

	@Test
	public void testAddCausedBy() throws Exception {
		DataConnection connection = new DataConnection();
		DataRow row = mock(DataRow.class);

		assertTrue(connection.getCausedBy().isEmpty());
		connection.addCausedBy(row);

		Mockito.verify(row, times(1)).addConnection(connection);
		assertTrue(connection.getCausedBy().contains(row));

	}

	@Test
	public void testAddResiltsIn() throws Exception {
		DataConnection connection = new DataConnection();
		DataRow row = mock(DataRow.class);

		assertTrue(connection.getResultsIn().isEmpty());
		connection.addResultsIn(row);

		Mockito.verify(row, times(1)).addConnection(connection);
		assertTrue(connection.getResultsIn().contains(row));
	}

	@Test
	public void testGetCausedBy() throws Exception {
		DataConnection connection = new DataConnection();
		DataRow row = new DataRow();
		Set<DataRow> result = new HashSet<>();
		result.add(row);

		connection.addCausedBy(row);
		assertEquals(connection.getCausedBy(), result);
	}

	@Test
	public void testGetResultsIn() throws Exception {
		DataConnection connection = new DataConnection();
		DataRow row = new DataRow();
		Set<DataRow> result = new HashSet<>();
		result.add(row);

		connection.addResultsIn(row);
		assertEquals(connection.getResultsIn(), result);
	}
}