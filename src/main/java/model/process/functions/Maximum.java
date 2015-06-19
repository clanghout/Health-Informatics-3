package model.process.functions;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;

import java.util.Optional;

/**
 * A class for finding the row with the maximum value for the specified column in a table.
 * 
 * @author Louis Gosschalk 11-05-2015
 */
public class Maximum extends Function<DataValue<?>> {

	public Maximum(DataTable table, DataDescriber<DataValue<?>> argument) {
		super(table, argument);
	}

	/**
	 * This function calculates the maximum.
	 *
	 * @return List<DataRow> the rows containing the minimum
	 */
	@Override
	public DataValue<?> calculate() {
		Optional<DataRow> row = getTable().getRows().stream()
				.max((thisRow, otherRow) ->
						getArgument().resolve(thisRow).compareTo(getArgument().resolve(otherRow)));
		return row.isPresent() ? getArgument().resolve(row.get()) : new FloatValue(0f);
	}

}
