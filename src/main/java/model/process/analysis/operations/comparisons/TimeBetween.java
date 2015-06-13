package model.process.analysis.operations.comparisons;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.InputMismatchException;

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

	private Identifier<DataColumn> date;
	private String dateName;
	private DataValue value;

	public TimeBetween(Identifier<DataColumn> date) {
		this.date = date;
		this.dateName = date.getName();
	}

	/**
	 * Check the type of the given column and call calculation.
	 */
	@Override
	public Table doProcess() {
		value = ((DataTable) getInput()).getRow(0).getValue(((DataTable) getInput()).getColumn(dateName));
		
		DataTableConversionBuilder builder = new DataTableConversionBuilder(((DataTable) getInput()), ((DataTable) getInput()).getName());
		builder.addRowsFromTable((DataTable) getInput());

		if (!value.getClass().equals(TimeValue.class)) {
			builder.addColumn((DataTable) getInput(), new PeriodValue(null,null, null), "Difference date");
		}
		if (!value.getClass().equals(DateValue.class)) {
			builder.addColumn((DataTable) getInput(), new TimeValue(null, null,null), "Difference time");
		}
		
		setInput(builder.build());
		
		DataTable result = calculateDiff((DataTable) getInput());
		
		System.out.println("COLUMN SIZE "+result.getColumns().size());
		
		return result;

	}

	private DataTable calculateDiff(DataTable table) {
		System.out.println("column type "+value.getClass().toString());
		
		table.getRow(0).setValue(table.getColumn("Difference date"),
				new PeriodValue(0, 0, 0));
		table.getRow(0).setValue(table.getColumn("Difference time"),
				new TimeValue(0, 0, 0));

		for (int i = 1; i < table.getRowCount(); i++) {
			DataRow current = table.getRow(i);
			DataRow previous = table.getRow(i - 1);

			LocalDate curdate = null;
			LocalTime curtime = null;
			LocalDate predate = null;
			LocalTime pretime = null;

			
			
			
			if (value.getClass().equals(DateTimeValue.class)) {
				System.out.println("calculating datetimevalue ");
				
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
				System.out.println("calculating datevalue ");
				
				curdate = (LocalDate) current.getValue(
						table.getColumn(dateName)).getValue();
				predate = (LocalDate) previous.getValue(
						table.getColumn(dateName)).getValue();
			}
			if (value.getClass().equals(TimeValue.class)) {
				System.out.println("calculating timevalue ");
				
				curtime = (LocalTime) current.getValue(
						table.getColumn(dateName)).getValue();
				pretime = (LocalTime) previous.getValue(
						table.getColumn(dateName)).getValue();
			}
			
			boolean negative = false;
			
			if (!value.getClass().equals(DateValue.class)) {
				Duration diffTime = Duration.between(pretime, curtime);
				int hour = (int) diffTime.toHours();
				System.out.println(hour);
				int min = ((int) diffTime.toMinutes()) - (hour * 60);
				System.out.println(min);
				int sec = ((int) diffTime.getSeconds()) - (min * 60)
						- (hour * 60 * 60);
				System.out.println(sec);
				//magically wizardize negative to positive.
				negative = checkNegative(hour, min, sec);
				DataValue val = new TimeValue(hour, min, sec);
				current.setValue(table.getColumn("Difference time"), val);
			}

			if (!value.getClass().equals(TimeValue.class)) {
				Period diffDate = Period.between(predate, curdate);
				if (negative) {
					diffDate.minusDays(1);
				}
				DataValue value = new PeriodValue(diffDate.getYears(),
						diffDate.getMonths(), diffDate.getDays());
				current.setValue(table.getColumn("Difference date"), value);
			}
		}
		return table;
	}
	
	private boolean checkNegative(int hour, int min, int sec) {
		
		if (hour < 0 || min < 0 || sec < 0) {
			
			return true;
		} else if ((hour < 0 || min < 0 || sec < 0) && datediff=0) {
			throw new InputMismatchException("Input table is not chronologically sorted.");
		}
		return false;
	}
}
