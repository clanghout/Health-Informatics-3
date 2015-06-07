package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.IntValue;
import model.data.value.PeriodValue;

import java.time.temporal.ChronoUnit;

/**
 * Created by Boudewijn on 7-6-2015.
 */
public class PeriodNode extends ValueNode<PeriodValue> {

	private final ValueNode<IntValue> amount;
	private final String unit;

	public PeriodNode(ValueNode<IntValue> amount, String unit) {
		super(null, null);
		this.amount = amount;
		this.unit = unit;
	}

	@Override
	public DataDescriber<PeriodValue> resolve(DataModel model) {
		Integer amountInt = amount.resolve(model).resolve(null).getValue();
		return new ConstantDescriber<>(
				PeriodValue.fromUnit(
						amountInt, resolveUnit(unit)
				)
		);
	}

	private ChronoUnit resolveUnit(String unit) {
		switch (unit) {
			case "DAYS":
				return ChronoUnit.DAYS;
			case "MONTH":
				return ChronoUnit.MONTHS;
			case "YEARS":
				return ChronoUnit.YEARS;
			default:
				throw new IllegalArgumentException(String.format("unit %s not supported", unit));
		}
	}
}
