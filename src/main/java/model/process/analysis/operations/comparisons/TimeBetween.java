package model.process.analysis.operations.comparisons;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.data.Table;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.PeriodValue;
import model.data.value.TimeValue;
import model.language.Identifier;
import model.process.DataProcess;

/**
 * This class will determine the time difference between rows. This is actually
 * a DataProcess but it's listed under comparisons in the requirements. ^ The
 * reason why it's located in the analysis package.
 * 
 * @author Louis Gosschalk 12-06-2015
 */
public class TimeBetween extends DataProcess {

	private String dateName;
	private DataValue value;
	private LocalDate curdate;
	private LocalTime curtime;
	private LocalDate predate;
	private LocalTime pretime;
	
	private static final int MINSEC = 60;

	public TimeBetween(Identifier<DataColumn> date) {
		this.dateName = date.getName();
	}

	/**
	 * Check the type of the given column and call calculation.
	 */
	@Override
	public Table doProcess() {
		value = ((DataTable) getInput()).getRow(0).getValue(
				((DataTable) getInput()).getColumn(dateName));

		DataTableConversionBuilder builder = new DataTableConversionBuilder(
				((DataTable) getInput()), ((DataTable) getInput()).getName());
		builder.addRowsFromTable((DataTable) getInput());

		if (!value.getClass().equals(TimeValue.class)) {
			builder.addColumn((DataTable) getInput(), new PeriodValue(null,
					null, null), "Difference date");
		}
		if (!value.getClass().equals(DateValue.class)) {
			builder.addColumn((DataTable) getInput(), new TimeValue(null, null,
					null), "Difference time");
		}

		return getVars(builder.build());
	}

	private DataTable getVars(DataTable table) {

		if (!value.getClass().equals(TimeValue.class)) {
			table.getRow(0).setValue(table.getColumn("Difference date"),
					new PeriodValue(0, 0, 0));
		}
		if (!value.getClass().equals(DateValue.class)) {
			table.getRow(0).setValue(table.getColumn("Difference time"),
					new TimeValue(0, 0, 0));
		}

		for (int i = 1; i < table.getRowCount(); i++) {
			DataRow current = table.getRow(i);
			DataRow previous = table.getRow(i - 1);

			if (value.getClass().equals(DateTimeValue.class)) {
				LocalDateTime cur = (LocalDateTime) current.getValue(
						table.getColumn(dateName)).getValue();
				curdate = cur.toLocalDate();
				curtime = cur.toLocalTime();

				LocalDateTime prev = (LocalDateTime) previous.getValue(
						table.getColumn(dateName)).getValue();
				predate = prev.toLocalDate();
				pretime = prev.toLocalTime();
			}
			if (value.getClass().equals(DateValue.class)) {
				curdate = (LocalDate) current.getValue(
						table.getColumn(dateName)).getValue();
				predate = (LocalDate) previous.getValue(
						table.getColumn(dateName)).getValue();
			}
			if (value.getClass().equals(TimeValue.class)) {
				curtime = (LocalTime) current.getValue(
						table.getColumn(dateName)).getValue();
				pretime = (LocalTime) previous.getValue(
						table.getColumn(dateName)).getValue();
			}

			table = calculate(table, current);

		}
		return table;
	}

	/**
	 * Do the actual calculating.
	 * 
	 * @param table
	 *            The table to set the calculated value in
	 * @param current
	 *            The row to set the calculated value in
	 * @return
	 */
	private DataTable calculate(DataTable table, DataRow current) {
		boolean negative = false;

		if (!value.getClass().equals(DateValue.class)) {
			Duration diffTime = Duration.between(pretime, curtime);

			negative = diffTime.isNegative();

			if (diffTime.isNegative()) {
				diffTime = diffTime.negated();
				diffTime = diffTime.minusDays(1);
				diffTime = diffTime.negated();
			}

			int hour = (int) diffTime.toHours();
			int min = ((int) diffTime.toMinutes()) - (hour * MINSEC);
			int sec = ((int) diffTime.getSeconds()) - (min * MINSEC)
					- (hour * MINSEC * MINSEC);

			DataValue val = new TimeValue(hour, min, sec);
			current.setValue(table.getColumn("Difference time"), val);
		}

		if (!value.getClass().equals(TimeValue.class)) {
			Period diffDate = Period.between(predate, curdate);
			if (negative) {
				diffDate = diffDate.minusDays(1);
			}
			DataValue value = new PeriodValue(diffDate.getYears(),
					diffDate.getMonths(), diffDate.getDays());
			current.setValue(table.getColumn("Difference date"), value);
		}

		return table;
	}
}
