package model.language.nodes;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.OperationDescriber;
import model.data.value.DateTimeValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.dates.computations.DateComputation;
import model.process.analysis.operations.dates.computations.ExtractDate;
import model.process.analysis.operations.dates.computations.ExtractTime;

/**
 * Represents the extraction of a part of a date time.
 * Created by Boudewijn on 17-6-2015.
 */
public class ExtractTimeNode extends OperationNode<TemporalValue<?>> {

	private ValueNode<DateTimeValue> dateTime;

	public ExtractTimeNode(String operation, ValueNode<DateTimeValue> dateTime) {
		super(null, operation, null);
		this.dateTime = dateTime;
	}

	@Override
	public DataDescriber<TemporalValue<?>> resolve(DataModel model) {
		return new OperationDescriber<TemporalValue<?>>(
				(DateComputation<DateTimeValue, TemporalValue<?>>) resolveExtraction(
						getOperation(),
						dateTime.resolve(model)
				)
		);
	}

	private DateComputation<DateTimeValue, ? extends TemporalValue<?>> resolveExtraction(
			String operation, DataDescriber<DateTimeValue> dateTime) {
		switch (operation) {
			case "TO_TIME": return new ExtractTime(dateTime);
			case "TO_DATE": return new ExtractDate(dateTime);
			default: throw new IllegalArgumentException(
					String.format("Operation %s not supported", operation)
			);
		}
	}
}
