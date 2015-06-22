package model.language.nodes;

import model.data.DataModel;
import model.process.describer.ConstantDescriber;
import model.process.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.data.value.IntValue;

/**
 * Represents a DateTime in the parser.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateTimeNode extends ValueNode<DateTimeValue> {

	private ValueNode<IntValue> year;
	private ValueNode<IntValue> month;
	private ValueNode<IntValue> day;
	private ValueNode<IntValue> hours;
	private ValueNode<IntValue> minutes;
	private ValueNode<IntValue> seconds;

	/**
	 * Construct a new DateTimeNode.
	 * @param year The year of the date time.
	 * @param month The month of the date time.
	 * @param day The day of the date time.
	 * @param hours The hour of the date time.
	 * @param minutes The minute of the date time.
	 * @param seconds The seconds of the date time.
	 */
	public DateTimeNode(
			ValueNode<IntValue> year,
	        ValueNode<IntValue> month,
	        ValueNode<IntValue> day,
	        ValueNode<IntValue> hours,
	        ValueNode<IntValue> minutes,
	        ValueNode<IntValue> seconds) {
		super(null, null);
		this.year = year;
		this.month = month;
		this.day = day;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	/**
	 * Resolves this node to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the date time in this node.
	 */
	@Override
	public DataDescriber<DateTimeValue> resolve(DataModel model) {
		return new ConstantDescriber<>(
				new DateTimeValue(
						year.resolve(model).resolve(null).getValue(),
						month.resolve(model).resolve(null).getValue(),
						day.resolve(model).resolve(null).getValue(),
						hours.resolve(model).resolve(null).getValue(),
						minutes.resolve(model).resolve(null).getValue(),
						seconds.resolve(model).resolve(null).getValue()
				)
		);
	}
}
