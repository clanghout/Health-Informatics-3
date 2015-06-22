package model.process.describer;

import model.data.DataModel;
import model.data.DataTable;
import model.data.Row;
import model.data.Table;
import model.data.value.DataValue;
import model.language.Identifier;
import model.process.functions.Function;

import java.util.Optional;

/**
 * Represents the operation of a Function.
 *
 * Created by Boudewijn on 10-6-2015.
 */
public class FunctionDescriber extends DataDescriber<DataValue<?>> {

	private DataModel model;
	private Identifier<Table> table;
	private Function function;

	/**
	 * Construct a new FunctionDescriber.
	 * @param model The model on which to operate.
	 * @param table The name of the table.
	 * @param function The function to use.
	 */
	public FunctionDescriber(DataModel model, Identifier<Table> table, Function function) {
		this.model = model;
		this.table = table;
		this.function = function;
	}

	/**
	 * Execute the function and return the result.
	 * @param row The row the value should be resolved from
	 * @return The result of the function.
	 */
	@Override
	public DataValue<?> resolve(Row row) {
		Optional<DataTable> tableOptional = model.getByName(table.getName());
		if (!tableOptional.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Table %s isn't present in model", table.getName())
			);
		}
		function.setTable(tableOptional.get());
		return function.calculate();
	}
}
