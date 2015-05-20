package language;

/**
 * Contains the information required to produce a DataProcess.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class ProcessInfo {

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
}
