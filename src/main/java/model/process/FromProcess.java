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
		Supplier<Stream<Optional<DataTable>>> tableStream = () ->
				Arrays.stream(identifiers)
				.map(x -> getDataModel().getByName(x.getName()));
		Optional<Optional<DataTable>> containsNull = tableStream.get()
				.filter(x -> !x.isPresent())
				.findAny();

		if (containsNull.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Not all identifiers refer to tables: %s",
							Arrays.toString(identifiers))
			);
		}

		Supplier<Stream<DataTable>> copied = () -> tableStream.get().map(x -> x.get().copy());

		Optional<DataTable> first = copied.get().findFirst();
		DataTable[] tables = copied.get().skip(1).toArray(DataTable[]::new);


		if (tables.length == 0) {
			return first.get();
		} else {
			return new CombinedDataTable(first.get(), tables);
		}
	}
}
