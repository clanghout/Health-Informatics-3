package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.StringValue;
import model.process.analysis.operations.constraints.CodeCheck;

/**
 * Created by Boudewijn on 3-6-2015.
 */
public class CodeCheckNode extends ValueNode<BoolValue> {

	private final ValueNode<StringValue> code;

	public CodeCheckNode(ValueNode<StringValue> code) {
		super(null, null);
		this.code = code;
	}

	@Override
	public DataDescriber<BoolValue> resolve(DataModel model) {
		return new ConstraintDescriber(new CodeCheck(code.resolve(model)));
	}
}
