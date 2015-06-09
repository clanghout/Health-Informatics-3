package model.process.setOperations;

import model.data.CombinedDataTable;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.process.DataProcess;
import model.process.analysis.ConstraintAnalysis;

/**
 * Perfomr a join on two tables. Set an optional constraint.
 * Created by jens on 6/9/15.
 */
public class Join extends DataProcess {
	DataTable table1;
	DataTable table2;
	ConstraintAnalysis constraint;
	String name;


	public Join(String name, DataTable table1, DataTable table2) {
		this.table1 = table1;
		this.table2 = table2;
		this.name = name;
		this.constraint = null;
	}

	public Join(String name, DataTable table1, DataTable table2, ConstraintAnalysis constraint) {
		this(name, table1, table2);
		this.constraint = constraint;
	}

	@Override
	protected Table doProcess() {
		return constraint.analyse(new CombinedDataTable(table1,table2).export(name));
	}
}
