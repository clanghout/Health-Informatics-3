package model.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * A container for the various data tables.
 * Created by Boudewijn on 12-5-2015.
 */
public class DataModel extends Observable implements Iterable<DataTable> {

	private List<DataTable> tables = new ArrayList<>();

	/**
	 * Construct a new empty DataModel.
	 */
	public DataModel() { }

	/**
	 * Construct a new DataModel containing the given tables.
	 * @param tables The tables you want to add to this data model.
	 */
	public DataModel(Collection<DataTable> tables) {
		this.tables.addAll(tables);
	}

	/**
	 * Get the iterator for this DataModel.
	 * @return An iterator over this DataModel.
	 */
	public Iterator<DataTable> iterator() {
		return tables.iterator();
	}

	/**
	 * Add the given table to this DataModel.
	 * @param table The table you want to add
	 */
	public void add(DataTable table) {
		insert(table);
		setUpdated();
	}

	private void insert(DataTable table) {
		Optional<DataTable> result = tables.stream()
				.filter(x -> x.getName().equals(table.getName()))
				.findFirst();
		if (result.isPresent()) {
			tables.set(tables.indexOf(result.get()), table);
		} else {
			tables.add(table);
		}
	}

	/**
	 * Add the given tables to this DataModel.
	 * @param tables The tables you want to add
	 */
	public void addAll(Collection<DataTable> tables) {
		tables.forEach(this::insert);
		setUpdated();
	}

	/**
	 * Returns the size of this DataModel.
	 * @return The number of tables in this DataModel.
	 */
	public int size() {
		return tables.size();
	}

	/**
	 * Check to see if this DataModel contains a table.
	 * @param table The table you want to check the containiness of.
	 * @return True if the DataModel contains the table, false if not.
	 */
	public boolean contains(DataTable table) {
		return tables.contains(table);
	}

	/**
	 * Get the index of the given table in the DataModel.
	 * @param table The table you want to get the index of.
	 * @return The index or -1 if the table isn't contained in this DataModel.
	 */
	public int indexOf(DataTable table) {
		return tables.indexOf(table);
	}

	/**
	 * Get the DataTable at the given index.
	 * @param index The index of the DataTable you want to get.
	 * @throws IndexOutOfBoundsException If the given index is out of bounds.
	 * @return The DataTable at the given index.
	 */
	public DataTable get(int index) {
		return tables.get(index);
	}

	/**
	 * Get the DataTable by the given name.
	 * @param name The name of the DataTable you want to get.
	 * @return The DataTable with the given name or null if none was found.
	 */
	public Optional<DataTable> getByName(String name) {
		return tables.stream()
				.filter(
						x -> x.getName().equals(name)
				).findFirst();
	}

	public void setUpdated() {
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns a new ObservableList with the DataTables that can be used
	 * by JavaFX components.
	 * @return The new ObservableList
	 */
	public ObservableList<DataTable> getObservableList() {
		return FXCollections.observableList(tables);
	}

	/**
	 * Get the tables contained in this model as a list.
	 * @return The tables in this list.
	 */
	public List<DataTable> getTables() {
		return Collections.unmodifiableList(tables);
	}
}
