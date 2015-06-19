package model.language.nodes;

import model.data.DataModel;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.describer.FunctionDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.DataValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.functions.*;

/**
 * Represents a Function in the parser.
 *
 * Created by Boudewijn on 10-6-2015.
 */
public class FunctionNode extends ValueNode<DataValue<?>> {

	private String function;
	private ColumnIdentifier tableColumn;

	/**
	 * Construct a new FunctionNode.
	 * @param function The name of the function you want to perform.
	 * @param tableColumn The column and table on which to perform the function.
	 */
	public FunctionNode(String function, ColumnIdentifier tableColumn) {
		super(null, null);
		this.function = function;
		this.tableColumn = tableColumn;
	}

	@Override
	public DataDescriber<DataValue<?>> resolve(DataModel model) {
		Function functionOperation = getFunction(model);

		Identifier<Table> table = new Identifier<>(tableColumn.getTable());
		return new FunctionDescriber(model, table, functionOperation);
	}

	public Function getFunction(DataModel model) {
		return resolveFunction(
				function,
				new TableValueDescriber<>(model, tableColumn)
		);
	}

	private Function resolveFunction(
			String function,
			DataDescriber<DataValue<?>> argument) {
		DataDescriber asNumber = (DataDescriber) argument;
		switch (function) {
			case "COUNT": return new Count(null, asNumber);
			case "AVERAGE": return new Average(null, asNumber);
			case "MIN": return new Minimum(null, argument);
			case "MAX": return new Maximum(null, argument);
			case "MEDIAN": return new Median(null, asNumber);
			case "SUM": return new Sum(null, argument);
			case "STDDEV": return new StandardDeviation(null, asNumber);
			default: throw new UnsupportedOperationException(
					String.format("Function %s isn't supported", function)
			);
		}
	}
}
