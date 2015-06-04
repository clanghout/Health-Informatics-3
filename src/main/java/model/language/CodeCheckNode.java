package model.language;

import model.data.DataModel;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.process.analysis.operations.constraints.CodeCheck;

/**
 * Created by Boudewijn on 3-6-2015.
 */
class CodeCheckNode extends BooleanNode {

	private final StringNode code;

	CodeCheckNode(StringNode code) {
		super(null, null);
		this.code = code;
	}

	@Override
	DataDescriber<BoolValue> resolve(DataModel model) {
		return new ConstraintDescriber(new CodeCheck(code.resolve(model)));
	}
}
