package model.data.process.functions;

import java.util.ArrayList;
import java.util.List;

import exceptions.FunctionInputMismatchException;
import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * This class will provide a framework for functions resulting a datavalue.
 * @author louisgosschalk
 *13-05-2015
 */
public abstract class Minmax extends Function {
  private DataTable table;
  private DataDescriber<NumberValue> argument;
  private List<DataRow> rowlist;
  
  public Minmax(DataTable model, DataDescriber<NumberValue> argument) {
    super(model, argument);
    this.table = model;
    this.argument = argument;
    this.rowlist = new ArrayList<DataRow>();
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
      DataRow first = table.getRow(0);
      Class<? extends DataValue> second = argument.resolve(first).getClass();
      //This is necessary because we're not sure it's either an int or float
      current = intOrFloat(argument, table.getRow(0));
      
      for (int i = 1; i < table.getRowCount(); i++) {
        float compare = 0.0f;
        //This is necessary because we're not sure it's either an int or float
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
