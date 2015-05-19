package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * A class for finding the row with the minimum value for the specified column in a model.
 * 
 * @author Louis Gosschalk 11-05-2015
 */
public class Minimum extends Minmax {

  public Minimum(DataTable model, DataDescriber<NumberValue> argument) {
    super(model, argument);
  }

  /**
   * Sets the function to determine the minimum in a comparison.
   */
  @Override
  public Boolean check(float comparison) {
    return comparison > 0;
  }
}
