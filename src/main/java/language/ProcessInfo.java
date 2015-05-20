package language;

import model.data.process.DataProcess;
import model.data.process.FromProcess;

/**
 * Contains the information required to produce a DataProcess.
 *
 * Created by Boudewijn on 20-5-2015.
 */
class ProcessInfo {

	private Identifier<Object> name;
	private Object[] parameters;

	public ProcessInfo(Identifier<Object> name, Object[] parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public Identifier<Object> getIdentifier() {
		return name;
	}

	public DataProcess resolve() {
		if (name.getName().equals("from")) {
			return new FromProcess((Identifier) parameters[0]);
		}
		throw new UnsupportedOperationException("This code has not been implemented yet");
	}
}
