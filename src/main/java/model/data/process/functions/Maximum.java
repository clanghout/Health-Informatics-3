package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * A class for finding the row with the maximum value for the specified column in a table.
 * 
 * @author Louis Gosschalk 11-05-2015
 */
public class Maximum extends Minmax {

  public Maximum(DataTable table, DataDescriber<NumberValue> argument) {
    super(table, argument);
  }

  /**
   * Sets the function to determine the maximum in a comparison.
   */
  @Override
  public Boolean check(float comparison) {
    return comparison < 0;
  }

}
