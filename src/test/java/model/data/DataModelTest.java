package model.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * The class containing the test cases for the DataModel.
 * Created by Boudewijn on 12-5-2015.
 */
public class DataModelTest {

	private DataModel model;

	private DataTable firstTable;
	private DataTable secondTable;
	private DataTable thirdTable;

	@Before
	public void setUp() throws Exception {
		model = new DataModel();

		firstTable = new DataTable("first");
		secondTable = new DataTable("second");
		thirdTable = new DataTable();
	}

	@Test
	public void testConstructor() throws Exception {
		model = new DataModel(Arrays.asList(
				firstTable,
				secondTable
		));
		assertTrue(model.contains(firstTable));
		assertEquals(1, model.indexOf(secondTable));
		assertFalse(model.contains(thirdTable));
	}

	@Test
	public void testIterator() throws Exception {
		model.add(firstTable);
		model.add(secondTable);

		Iterator<DataTable> it = model.iterator();
		assertEquals(firstTable, it.next());
		assertEquals(secondTable, it.next());
		assertFalse(it.hasNext());
	}

	@Test
	public void testAdd() throws Exception {
		model.add(firstTable);
		model.add(secondTable);

		assertEquals(firstTable, model.get(0));
		assertEquals(secondTable, model.get(1));
	}

	@Test
	public void testGet() throws Exception {
		model.add(firstTable);
		assertEquals(firstTable, model.get(0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet2() throws Exception {
		model.get(0);
	}

	@Test
	public void testAddAll() throws Exception {
		model.addAll(Arrays.asList(
				firstTable,
				secondTable,
				thirdTable
		));

		assertTrue(model.contains(firstTable));
		assertTrue(model.contains(secondTable));
		assertTrue(model.contains(thirdTable));
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(0, model.size());

		model.add(firstTable);
		assertEquals(1, model.size());

		model.add(secondTable);
		assertEquals(2, model.size());

		model.add(thirdTable);
		assertEquals(3, model.size());
	}

	@Test
	public void testContains() throws Exception {
		model.add(firstTable);

		assertTrue(model.contains(firstTable));
		assertFalse(model.contains(secondTable));
	}

	@Test
	public void testIndexOf() throws Exception {
		model.add(firstTable);

		assertEquals(0, model.indexOf(firstTable));
		assertEquals(-1, model.indexOf(secondTable));
	}

	@Test
	public void testGetByName() throws Exception {
		model.add(firstTable);
		model.add(secondTable);

		assertEquals(firstTable, model.getByName(firstTable.getName()).get());
		assertEquals(secondTable, model.getByName(secondTable.getName()).get());
	}
}