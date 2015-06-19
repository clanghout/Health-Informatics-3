package model.language.nodes;

import model.data.DataModel;
import model.process.describer.ConstantDescriber;
import model.process.describer.DataDescriber;
import model.data.value.IntValue;
import model.data.value.TimeValue;

/**
 * Created by Boudewijn on 16-6-2015.
 */
public class TimeNode extends ValueNode<TimeValue> {

	private ValueNode<IntValue> hours;
	private ValueNode<IntValue> minutes;
	private ValueNode<IntValue> seconds;

	public TimeNode(
			ValueNode<IntValue> hours,
			ValueNode<IntValue> minutes,
			ValueNode<IntValue> seconds) {
		super(null, null);
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	@Override
	public DataDescriber<TimeValue> resolve(DataModel model) {
		return new ConstantDescriber<>(new TimeValue(
				hours.resolve(model).resolve(null).getValue(),
				minutes.resolve(model).resolve(null).getValue(),
				seconds.resolve(model).resolve(null).getValue()
		));
	}
}
