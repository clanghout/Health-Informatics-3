package model.process.analysis;

import model.data.DataRow;
import model.data.DataTable;
import model.data.Row;
import model.data.Table;
import model.process.analysis.operations.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Analyse that sets a code on the table.
 *
 * User specifies the constraint, and on which tables the code must be set.
 *
 * Created by jens on 5/27/15.
 */
public class CodeAnalysis extends DataAnalysis {
	private Event event;
	private String code;
	private List<DataTable> tables;

	public CodeAnalysis(Event event, String code, DataTable... tables) {
		this.event = event;
		this.code = code;
		this.tables = new ArrayList<>(Arrays.asList(tables));
	}

	public void setTables(DataTable... tables) {
		this.tables = new ArrayList<>(Arrays.asList(tables));
	}

	public void addTable(DataTable table) {
		this.tables.add(table);
	}


	@Override
	public Table analyse(Table input) {
		Iterator<? extends Row> rows = input.iterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			//TODO check if it contains the event
			if (true) {
				input.flagRow(row);
			}
		}
		for(DataTable table : tables) {
			for (DataRow row : table.getFlaggedRows()) {
				row.addCode(code);
			}
		}

		input.resetFlags();
		return input;
	}
}
