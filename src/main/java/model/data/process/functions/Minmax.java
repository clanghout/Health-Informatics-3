package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

/**
 * This class will provide a framework for functions resulting a datavalue.
 * @author louisgosschalk
 *13-05-2015
 */
public abstract class Minmax extends Function {
  private DataTable table;
  private DataDescriber<NumberValue> argument;
  
  public Minmax(DataTable model, DataDescriber<NumberValue> argument) {
    super(model, argument);
    this.table = model;
    this.argument = argument;
  }
  /**
   * This function checks restrictions for determining minimum & maximum.
   * @return List<DataRow> the rows containing the minimum
   */
  public DataValue calculate() {
    initialize();
    return compare();
  }
  
  /**
   * This function calculates minimum or maximum through a generic arithmetic calculation.
   * @return List<DataRow> a list of DataRows
   */
    public DataValue compare() {
      float current = 0.0f;
      current = intOrFloat(argument, table.getRow(0));
      for (int i = 1; i < table.getRowCount(); i++) {
        float compare = 0.0f;
        compare = intOrFloat(argument, table.getRow(i));
        float comparison = current - compare;
        if (check(comparison)) {
          current = compare;
        }
      }
      FloatValue result = new FloatValue(current);
      return result;
    }
    public abstract Boolean check(float comparison);
}
