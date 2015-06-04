package model.language;

import model.data.DataModel;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.StringValue;

/**
 * Created by Boudewijn on 3-6-2015.
 */
class StringConstantNode extends StringNode {

	private final String content;

	StringConstantNode(String string) {
		super(null, null);
		content = string;
	}

	@Override
	DataDescriber<StringValue> resolve(DataModel model) {
		return new ConstantDescriber<>(new StringValue(content));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		StringConstantNode that = (StringConstantNode) o;

		return content.equals(that.content);

	}

	@Override
	public int hashCode() {
		return content.hashCode();
	}
}
