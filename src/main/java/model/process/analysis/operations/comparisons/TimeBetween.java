package model.process.analysis.operations.comparisons;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import model.data.DataColumn;
import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.TimeValue;
import model.language.Identifier;

/**
 * This class will determine the time difference between rows.
 * 
 * @author Louis Gosschalk 12-06-2015
 */
public class TimeBetween {

	private DataTable table;
	private Identifier<DataColumn> date;
	private String dateName;

	public TimeBetween(DataTable table, Identifier<DataColumn> date) {
		this.table = table;
		this.date = date;
		this.dateName = date.getName();
	}

	/**
	 * Check the type of the given column and call calculation.
	 */
	public void process() {
		DataTableConversionBuilder builder = 
				new DataTableConversionBuilder(table, table.getName());
		builder.addColumn(table, 
				new DateTimeValue(null, null, null, null, null, null), "Difference");
		
		calculateDiff();

	}

	private void calculateDiff() {
		table.getRow(0).setValue(table.getColumn(dateName), new DateTimeValue(0, 0, 0, 0, 0, 0));
		for (int i = 1; i < table.getRowCount(); i++) {
			DataRow current = table.getRow(i);
			DataRow previous = table.getRow(i-1);
			
			int year = 0; 
			int month = 0; 
			int day = 0; 
			int hour = 0; 
			int minute = 0; 
			int second = 0;
			
			if (date.getClass().equals(DateTimeValue.class)) {
				LocalDateTime cur = (LocalDateTime) current.getValue(table.getColumn(dateName)).getValue();
				LocalDateTime prev = (LocalDateTime) previous.getValue(table.getColumn(dateName)).getValue();
				year = cur.getYear() - prev.getYear();
				month = cur.getMonthValue() - prev.getMonthValue();
				day = cur.getDayOfMonth() - prev.getDayOfMonth();
				hour = cur.getHour() - prev.getHour();
				minute = cur.getMinute() - prev.getMinute();
				second = cur.getSecond() - prev.getSecond();
			}
			if (date.getClass().equals(DateValue.class)) {
				LocalDate cur = (LocalDate) current.getValue(table.getColumn(dateName)).getValue();
				LocalDate prev = (LocalDate) previous.getValue(table.getColumn(dateName)).getValue();
				year = cur.getYear() - prev.getYear();
				month = cur.getMonthValue() - prev.getMonthValue();
				day = cur.getDayOfMonth() - prev.getDayOfMonth();
			}
			if (date.getClass().equals(TimeValue.class)) {
				LocalTime cur = (LocalTime) current.getValue(table.getColumn(dateName)).getValue();
				LocalTime prev = (LocalTime) previous.getValue(table.getColumn(dateName)).getValue();
				hour = cur.getHour() - prev.getHour();
				minute = cur.getMinute() - prev.getMinute();
				second = cur.getSecond() - prev.getSecond();
			}
			
			
			LocalDateTime diff = LocalDateTime.of(year, month, day, hour, minute, second);
			
			DataValue input = new DateTimeValue(diff);
			current.setValue(table.getColumn(dateName), input);
		}
	}
}
