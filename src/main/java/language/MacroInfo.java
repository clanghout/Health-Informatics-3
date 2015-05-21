package language;

import model.data.DataModel;
import model.data.process.analysis.operations.Operation;

import java.util.List;

/**
 * Created by Boudewijn on 21-5-2015.
 */
class MacroInfo {

	private Identifier identifier;
	private List<Object> params;
	private MacroType type;
	private String body;

	MacroInfo(Identifier identifier, List<Object> params, MacroType type, String body) {
		this.identifier = identifier;
		this.params = params;
		this.type = type;
		this.body = body;
	}

	public Operation parse(DataModel model) {
		return type.parse(body, model);
	}
}
