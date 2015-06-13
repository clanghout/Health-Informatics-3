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
 * This class will determine the time difference between rows.
 * This is actually a DataProcess but it's listed under comparisons in the requirements.
 * ^ The reason why it's located in the analysis package.
 * @author Louis Gosschalk 12-06-2015
 */
public class TimeBetween extends DataProcess {

	private DataTable table;
	private Identifier<DataColumn> date;
	private String dateName;

	public TimeBetween(Identifier<DataColumn> date) {
		this.table = (DataTable) getInput();
		this.date = date;
		this.dateName = date.getName();
	}

	/**
	 * Check the type of the given column and call calculation.
	 */
	@Override
	public Table doProcess() {
		DataTableConversionBuilder builder = 
				new DataTableConversionBuilder(table, table.getName());

		if (!date.getClass().equals(TimeValue.class)) {
			builder.addColumn(table, 
					new PeriodValue(null, null, null), "Difference date");
		}
		if (!date.getClass().equals(DateValue.class)) {
			builder.addColumn(table, 
					new TimeValue(null, null,null), "Difference time");
		}
		
		table = builder.build();
		calculateDiff();
		
		return table;
		
	}

	private void calculateDiff() {
		table.getRow(0).setValue(table.getColumn("Difference date"), new PeriodValue(0, 0, 0));
		table.getRow(0).setValue(table.getColumn("Difference time"), new TimeValue(0, 0, 0));
		
		for (int i = 1; i < table.getRowCount(); i++) {
			DataRow current = table.getRow(i);
			DataRow previous = table.getRow(i-1);
			
			LocalDate curdate = null;
			LocalTime curtime = null;
			LocalDate predate = null;
			LocalTime pretime = null;
			
			if (date.getClass().equals(DateTimeValue.class)) {
				LocalDateTime cur = (LocalDateTime) current.getValue(table.getColumn(dateName)).getValue();
				curdate = cur.toLocalDate();
				curtime = cur.toLocalTime();
				LocalDateTime prev = (LocalDateTime) previous.getValue(table.getColumn(dateName)).getValue();
				predate = cur.toLocalDate();
				pretime = cur.toLocalTime();
			}
			if (date.getClass().equals(DateValue.class)) {
				curdate = (LocalDate) current.getValue(table.getColumn(dateName)).getValue();
				predate = (LocalDate) previous.getValue(table.getColumn(dateName)).getValue();
			}
			if (date.getClass().equals(TimeValue.class)) {
				curtime = (LocalTime) current.getValue(table.getColumn(dateName)).getValue();
				pretime = (LocalTime) previous.getValue(table.getColumn(dateName)).getValue();
			}
			
			if (!date.getClass().equals(TimeValue.class)) {
				Period diffDate = Period.between(predate, curdate);
				DataValue value = new PeriodValue(diffDate.getYears(), diffDate.getMonths(), diffDate.getDays());
				current.setValue(table.getColumn("Difference date"), value);
			}
			if (!date.getClass().equals(DateValue.class)) {
				Duration diffTime = Duration.between(pretime, curtime);
				//CONVERTEER VAN SECONDES NAAR UREN,MINUTEN,SECONDES
				int hour = (int) diffTime.toHours();
				int min = ((int) diffTime.toMinutes()) - (hour*60);
				int sec = ((int) diffTime.getSeconds()) - (min*60) - (hour*60*60);
				DataValue value = new TimeValue(hour, min, sec);
				current.setValue(table.getColumn("Difference time"), value);
			}
		}
	}
}
