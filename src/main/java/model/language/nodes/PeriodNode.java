package model.language.nodes;

import model.data.DataModel;
import model.process.describer.ConstantDescriber;
import model.process.describer.DataDescriber;
import model.data.value.IntValue;
import model.data.value.PeriodValue;

import java.time.temporal.ChronoUnit;

/**
 * Represents a ValueNode containing a Period.
 *
 * Created by Boudewijn on 7-6-2015.
 */
public class PeriodNode extends ValueNode<PeriodValue> {

	private final ValueNode<IntValue> amount;
	private final String unit;

	/**
	 * Construct a new PeriodNode.
	 * @param amount The amount of time in the given unit.
	 * @param unit The unit of time.
	 */
	public PeriodNode(ValueNode<IntValue> amount, String unit) {
		super(null, null);
		this.amount = amount;
		this.unit = unit;
	}

	/**
	 * Resolves the value in this PeriodNode.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the value in this node.
	 */
	@Override
	public DataDescriber<PeriodValue> resolve(DataModel model) {
		Integer amountInt = amount.resolve(model).resolve(null).getValue();
		return new ConstantDescriber<>(
				PeriodValue.fromUnit(
						amountInt, resolveUnit(unit)
				)
		);
	}

	static ChronoUnit resolveUnit(String unit) {
		switch (unit) {
			case "DAYS":
				return ChronoUnit.DAYS;
			case "MONTH":
				return ChronoUnit.MONTHS;
			case "YEARS":
				return ChronoUnit.YEARS;
			case "HOURS":
				return ChronoUnit.HOURS;
			case "MINUTES":
				return ChronoUnit.MINUTES;
			case "SECONDS":
				return ChronoUnit.SECONDS;
			default:
				throw new IllegalArgumentException(String.format("unit %s not supported", unit));
		}
	}
}
