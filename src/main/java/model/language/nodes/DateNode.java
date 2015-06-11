package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.DateValue;
import model.data.value.IntValue;

/**
 * Represents a Date in the parser.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateNode extends ValueNode<DateValue> {

	private ValueNode<IntValue> year;
	private ValueNode<IntValue> month;
	private ValueNode<IntValue> day;

	/**
	 * Construct a new DateNode.
	 * @param year The year of the date.
	 * @param month The month of the date.
	 * @param day The day of the date.
	 */
	public DateNode(
			ValueNode<IntValue> year,
			ValueNode<IntValue> month,
			ValueNode<IntValue> day) {
		super(null, null);
		this.year = year;
		this.month = month;
		this.day = day;
	}

	/**
	 * Resolves the DateNode to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the date in this node.
	 */
	@Override
	public DataDescriber<DateValue> resolve(DataModel model) {
		return new ConstantDescriber<>(
				new DateValue(
						year.resolve(model).resolve(null).getValue(),
						month.resolve(model).resolve(null).getValue(),
						day.resolve(model).resolve(null).getValue()
				)
		);
	}
}
