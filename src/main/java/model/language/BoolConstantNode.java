package model.language;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;

/**
 * Created by Boudewijn on 26-5-2015.
 */
class BoolConstantNode extends BooleanNode {

	private DataDescriber<BoolValue> value;

	BoolConstantNode(boolean value) {
		super(null, null);
		this.value = new ConstantDescriber<>(new BoolValue(value));
	}

	DataDescriber<BoolValue> resolve(DataModel model) {
		return value;
	}

}
