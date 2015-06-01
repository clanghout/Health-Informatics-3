package model.process.analysis.operations;

import model.data.CombinedDataTable;
import model.data.DataRow;
import model.data.DataTable;
import model.data.Table;
import model.process.DataProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class defines an event.
 * Created by jens on 5/28/15.
 */
public class EventDefinition {
	private String name;
	private DataProcess process;
	private List<DataTable> tables;

	/**
	 * Create a new event.
	 * @param name the name of the event.
	 * @param process process that filters the rows that contain the event
	 * @param tables tables that must get the code for the event
	 */
	public EventDefinition(String name, DataProcess process, DataTable... tables) {
		this.name = name;
		this.tables = new ArrayList<>(Arrays.asList(tables));
		this.process = process;
	}

	/**
	 * set the code on the rows that corresponds to this event.
	 */
	public void setCode() {
		Table table = process.process();

		for (DataTable codeTable : tables) {
			if (table instanceof DataTable
					&& ((DataTable) table).getName().equals(codeTable.getName())) {
				setCode(codeTable, (DataTable) table);
			} else if (table instanceof CombinedDataTable) {
				DataTable dataTable = ((CombinedDataTable) table).getTable(codeTable.getName());
				if (dataTable == null) {
					throw new IllegalArgumentException("event does not contain the table"
							+ "that must get a code");
				} else {
					setCode(codeTable, dataTable);
				}
			} else {
				throw new IllegalArgumentException("event does not contain the table"
						+ "that must get a code");
			}
		}
	}

	/**
	 * set the eventcode on a row in the codeTable if the filteredTable contains that row.
	 * @param codeTable table that must get the rows
	 * @param filteredTable table that contains the rows that must get a code
	 */
	private void setCode(DataTable codeTable, DataTable filteredTable) {
		for (DataRow filteredRows : filteredTable.getRows()) {
			for (DataRow codeRow : codeTable.getRows()) {
				if (filteredRows.equalsSoft(codeRow)) {
					codeRow.addCode(name);
					break;
				}
			}
		}
	}


}

