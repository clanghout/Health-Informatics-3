package model.data.process.functions;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.describer.RowValueDescriber;

import org.junit.Before;
import org.junit.Test;

import exceptions.FunctionInputMismatchException;

/**
 * Standard tests which should pass on all types of functions.
 * 
 * @author Louis Gosschalk 19-05-2015
 */
public class FunctionTest {
  private DataTable table;
  private DataColumn column;
  
  @Before
  public void setUp() {
    table = new DataTable();
  }
  
  @Test(expected = FunctionInputMismatchException.class)
  public void testEmptyTableAverage() throws Exception {
    Average f = new Average(table, new RowValueDescriber<>(column));
  }
  
  @Test(expected = FunctionInputMismatchException.class)
  public void testEmptyTableMaximum() throws Exception {
    Maximum f = new Maximum(table, new RowValueDescriber<>(column));
  }
  
  @Test(expected = FunctionInputMismatchException.class)
  public void testEmptyTableMinimum() throws Exception {
    Minimum f = new Minimum(table, new RowValueDescriber<>(column));
  }
  
  @Test(expected = FunctionInputMismatchException.class)
  public void testEmptyTableMedian() throws Exception {
    Median f = new Median(table, new RowValueDescriber<>(column));
  }
  
  @Test(expected = FunctionInputMismatchException.class)
  public void testEmptyTableSum() throws Exception {
    Sum f = new Sum(table, new RowValueDescriber<>(column));
  }
}
