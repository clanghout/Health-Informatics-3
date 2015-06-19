package model.process.functions;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;

import java.util.Optional;

/**
 * A class for finding the row with the minimum value for the specified column in a model.
 * 
 * @author Louis Gosschalk 11-05-2015
 */
public class Minimum extends Function<DataValue<?>> {

	public Minimum(DataTable table, DataDescriber<DataValue<?>> argument) {
		super(table, argument);
	}

	/**
	 * This function calculates the minimum.
	 *
	 * @return List<DataRow> the rows containing the minimum
	 */
	@Override
	public DataValue<?> calculate() {
		Optional<DataRow> row = getTable().getRows().stream()
				.min((thisRow, otherRow) ->
						getArgument().resolve(thisRow).compareTo(getArgument().resolve(otherRow)));
		return row.isPresent() ? getArgument().resolve(row.get()) : new FloatValue(0f);
	}
}
