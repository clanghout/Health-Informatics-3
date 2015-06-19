package model.language.nodes;

import model.data.DataModel;
import model.process.describer.ConstraintDescriber;
import model.process.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.StringValue;
import model.process.analysis.operations.constraints.CodeCheck;

/**
 * Represent the code check operation on a row.
 *
 * Created by Boudewijn on 3-6-2015.
 */
public class CodeCheckNode extends ValueNode<BoolValue> {

	private final ValueNode<StringValue> code;

	/**
	 * Construct a new CodeCheckNode.
	 * @param code The code you want to check for.
	 */
	public CodeCheckNode(ValueNode<StringValue> code) {
		super(null, null);
		this.code = code;
	}

	/**
	 * Resolves the code check to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the code check.
	 */
	@Override
	public DataDescriber<BoolValue> resolve(DataModel model) {
		return new ConstraintDescriber(new CodeCheck(code.resolve(model)));
	}
}
