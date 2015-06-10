package model.process.analysis.operations;

import model.data.DataColumn;
import model.data.DataTable;

import model.data.describer.ConstantDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.DataProcess;
import model.process.SortProcess;
import model.process.setOperations.FullJoin;


/**
 * Class that is able to constuct a connection table.
 * Created by jens on 6/10/15.
 */
public class Connection extends DataProcess{

	private ColumnIdentifier timeColumnIdentifier;
	private DataColumn timeColumn;
	private FullJoin join;

	public Connection(
			String name,
			Identifier<DataTable> left,
			ColumnIdentifier timeColumnLeft,
			Identifier<DataTable> right,
			ColumnIdentifier timeColumnRght) {
		join = new FullJoin(name, left, right, FullJoin.Join.FULL);

		this.timeColumnIdentifier = timeColumnLeft;
		join.addCombineColumn(timeColumnRght, timeColumnLeft);
		join.setConstraint(new ConstantDescriber<>(new BoolValue(false)));
	}

	public void addOverlappingColumns(ColumnIdentifier origin, ColumnIdentifier mapTo) {
		join.addCombineColumn(origin, mapTo);
	}


	@Override
	protected DataTable doProcess() {
		timeColumn = getDataModel().getByName(timeColumnIdentifier.getTable()).get()
				.getColumn(timeColumnIdentifier.getColumn());
		join.setDataModel(this.getDataModel());
		DataTable joinedTable = (DataTable) join.process();
		SortProcess sort = new SortProcess(new RowValueDescriber<>(join.getNewColumn(timeColumn))
				, SortProcess.Order.ASCENDING);
		sort.setDataModel(this.getDataModel());
		sort.setDataModel(getDataModel());
		sort.setInput(joinedTable);

		return (DataTable) sort.process();
	}
}
