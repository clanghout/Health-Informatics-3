package model.process.analysis.operations;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.Table;
import model.data.describer.ConstantDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.language.Identifier;
import model.process.DataProcess;
import model.process.SortProcess;
import model.process.setOperations.FullJoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jens on 6/10/15.
 */
public class Connection extends DataProcess{

	private DataColumn timeColumn;
	private FullJoin join;

	public Connection(
			String name,
			Identifier<DataTable> left,
			DataColumn timeColumnLeft,
			Identifier<DataTable> right,
			DataColumn timeColumnRght) {
		join = new FullJoin(name, left, right, FullJoin.Join.FULL);

		this.timeColumn = timeColumnLeft;
		join.addCombineColumn(timeColumnRght, timeColumnLeft);
		join.setConstraint(new ConstantDescriber<>(new BoolValue(false)));
	}

	public void addOverlappingColumns(DataColumn origin, DataColumn mapTo) {
		join.addCombineColumn(origin, mapTo);
	}


	@Override
	protected DataTable doProcess() {
		DataTable joinedTable = (DataTable) join.process();
		SortProcess sort = new SortProcess(new RowValueDescriber<>(join.getNewColumn(timeColumn))
				, SortProcess.Order.ASCENDING);
		sort.setDataModel(getDataModel());
		sort.setInput(joinedTable);

		return (DataTable) sort.process();
	}
}
