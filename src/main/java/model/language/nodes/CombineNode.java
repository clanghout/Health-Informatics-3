package model.language.nodes;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.OperationDescriber;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.TimeValue;
import model.process.analysis.operations.dates.computations.Combine;

/**
 * Represents the combination of a date and time into a datetime.
 *
 * Created by Boudewijn on 17-6-2015.
 */
public class CombineNode extends ValueNode<DateTimeValue> {

	private ValueNode<DateValue> dateValue;
	private ValueNode<TimeValue> timeValue;

	/**
	 * Construct a new CombineNode.
	 * @param dateValue The date to combine.
	 * @param timeValue The time to combine.
	 */
	public CombineNode(
			ValueNode<DateValue> dateValue,
	        ValueNode<TimeValue> timeValue) {
		super(null, null);
		this.dateValue = dateValue;
		this.timeValue = timeValue;
	}

	@Override
	public DataDescriber<DateTimeValue> resolve(DataModel model) {
		return new OperationDescriber<>(
				new Combine(
						dateValue.resolve(model),
						timeValue.resolve(model)
				)
		);
	}
}
