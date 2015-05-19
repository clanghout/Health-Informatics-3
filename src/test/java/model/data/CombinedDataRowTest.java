//package model.data;
//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
///**
// * Created by jens on 5/18/15.
// */
//public class CombinedDataRowTest {
//
//	@Test
//	public void testAddDataRow() throws Exception {
//		CombinedDataRow combinedDataRow = new CombinedDataRow();
//		assertNull(combinedDataRow.getRow("test"));
//		combinedDataRow.addDataRow(new DataRow(), "test");
//		assertNotNull(combinedDataRow.getRow("test"));
//	}
//
//	@Test
//	public void testGetRow() throws Exception {
//		CombinedDataRow combinedDataRow = new CombinedDataRow();
//		DataRow row1 = new DataRow();
//		DataRow row2 = new DataRow();
//		combinedDataRow.addDataRow(row1, "test");
//		assertEquals(combinedDataRow.getRow("test"), row1);
//		combinedDataRow.addDataRow(row2, "test2");
//		assertEquals(combinedDataRow.getRow("test2"), row2);
//	}
//}