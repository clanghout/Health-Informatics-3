package model.process;

import model.data.CombinedDataTable;
import model.language.Identifier;
import model.data.DataTable;
import model.data.Table;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This process simply reads a table from memory and outputs it.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class FromProcess extends DataProcess {

	private Identifier<DataTable>[] identifiers;

	public FromProcess(Identifier<DataTable>... identifiers) {
		if (identifiers.length == 0) {
			throw new IllegalArgumentException("identifiers is empty");
		}
		this.identifiers = identifiers;
	}

	@Override
	protected Table doProcess() {
		Supplier<Stream<DataTable>> tableStream = () ->
				Arrays.stream(identifiers)
				.map(x -> getDataModel().getByName(x.getName()).copy());
		Optional<DataTable> first = tableStream.get().findFirst();
		DataTable[] tables = tableStream.get().skip(1).toArray(DataTable[]::new);

		if (tables.length == 0) {
			return first.get();
		} else {
			return new CombinedDataTable(first.get(), tables);
		}
	}
}
