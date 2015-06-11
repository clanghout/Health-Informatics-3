package model.process.analysis.operations;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Row;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.process.DataProcess;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jens on 6/11/15.
 */
public class ColumnComputation extends DataProcess{


	private String name;
	private LinkedHashMap<String, DataDescriber<? extends DataValue>> describers;

	public ColumnComputation(String name) {
		this.name = name;
	}

	public void addColumn(DataDescriber<? extends DataValue> describer, String name) {
		describers.put(name, describer);
	}

	@Override
	protected Table doProcess() {
		Table input = getInput();
		Iterator<? extends Row> iterator = input.iterator();
		if (! iterator.hasNext()) {
			throw new IllegalStateException("Table has no rows");
		}

		Row row = iterator.next();
		DataTableBuilder builder = constructBuilder(row);

		builder.createRow(getValues(row));
		while(iterator.hasNext()) {
			builder.createRow(getValues(iterator.next()));
		}

		return builder.build();
	}

	private DataTableBuilder constructBuilder(Row row) {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName(name);

		for (Map.Entry<String, DataDescriber<? extends DataValue>> entry : describers.entrySet()) {
			builder.createColumn(entry.getKey(), entry.getValue().resolve(row).getClass());
		}

		return builder;
	}

	private DataValue[] getValues(Row row) {
		DataValue[] values = new DataValue[describers.size()];
		int i = 0;
		for (Map.Entry<String, DataDescriber<? extends DataValue>> entry : describers.entrySet()) {
			values[i] = entry.getValue().resolve(row);
			i++;
		}
		return values;
	}

}
