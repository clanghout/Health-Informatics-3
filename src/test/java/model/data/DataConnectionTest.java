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

	@Test
	public void testEqualsEmpty() throws Exception {
		DataConnection connection1 = new DataConnection();
		DataConnection connection2 = new DataConnection();

		assertTrue(connection1.equals(connection2));
	}

	@Test
	public void testEqualsTrue() throws Exception {
		DataConnection connection1 = new DataConnection();
		DataConnection connection2 = new DataConnection();
		DataRow row1 = new DataRow();
		DataRow row2 = new DataRow();
		DataRow row3 = new DataRow();
		DataRow row4 = new DataRow();

		connection1.addCausedBy(row1);
		connection1.addCausedBy(row2);
		connection1.addResultsIn(row3);
		connection1.addResultsIn(row4);

		connection2.addCausedBy(row1);
		connection2.addCausedBy(row2);
		connection2.addResultsIn(row3);
		connection2.addResultsIn(row4);

		assertTrue(connection1.equals(connection2));
	}

	@Test
	public void testEqualsResultFalse() throws Exception {
		DataConnection connection1 = new DataConnection();
		DataConnection connection2 = new DataConnection();
		DataRow row1 = new DataRow();
		DataRow row2 = new DataRow();
		DataRow row3 = new DataRow();
		DataRow row4 = new DataRow();

		connection1.addCausedBy(row1);
		connection1.addCausedBy(row2);
		connection1.addResultsIn(row4);

		connection2.addCausedBy(row1);
		connection2.addCausedBy(row2);
		connection2.addResultsIn(row3);
		connection2.addResultsIn(row4);

		assertFalse(connection1.equals(connection2));
	}

	@Test
	public void testEqualsTCausedByFalse() throws Exception {
		DataConnection connection1 = new DataConnection();
		DataConnection connection2 = new DataConnection();
		DataRow row1 = new DataRow();
		DataRow row2 = new DataRow();
		DataRow row3 = new DataRow();
		DataRow row4 = new DataRow();

		connection1.addCausedBy(row1);
		connection1.addCausedBy(row2);
		connection1.addResultsIn(row3);
		connection1.addResultsIn(row4);

		connection2.addCausedBy(row2);
		connection2.addResultsIn(row3);
		connection2.addResultsIn(row4);

		assertFalse(connection1.equals(connection2));
	}

	@Test
	public void testHashCode() throws Exception {
		DataConnection connection1 = new DataConnection();
		DataConnection connection2 = new DataConnection();
		DataRow row1 = new DataRow();
		DataRow row2 = new DataRow();
		DataRow row3 = new DataRow();
		DataRow row4 = new DataRow();

		connection1.addCausedBy(row1);
		connection1.addCausedBy(row2);
		connection1.addResultsIn(row3);
		connection1.addResultsIn(row4);

		connection2.addCausedBy(row1);
		connection2.addResultsIn(row3);

		assertNotEquals(connection1.hashCode(), connection2.hashCode(), 0.1);

		connection2.addCausedBy(row2);
		connection2.addResultsIn(row4);

		assertEquals(connection1.hashCode(), connection2.hashCode(), 0.1);
	}
}