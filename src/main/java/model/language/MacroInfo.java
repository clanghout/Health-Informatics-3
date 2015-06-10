package model.language;

import model.data.DataModel;


/**
 * Created by Boudewijn on 21-5-2015.
 */
class MacroInfo {

	private Identifier identifier;
	private MacroType type;
	private String body;

	MacroInfo(Identifier identifier, MacroType type, String body) {
		this.identifier = identifier;
		this.type = type;
		this.body = body;
	}

	Object parse(DataModel model) {
		return type.parse(body, model);
	}

	Identifier getIdentifier() {
		return identifier;
	}
}
