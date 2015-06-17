package model.data;

/**
 * In most parts of the program we need a single datamodel.
 * However some places require a new Datamodel, so we can't make datamodel singletond.
 * This class is a singleton class that return always the same datamodel.
 *
 * Created by jens on 6/17/15.
 */
public class ProgramModel {
	volatile private static DataModel model;

	public static DataModel getDataModel() {
		if (model == null) {
			synchronized (DataModel.class) {
				if (model == null) {
					model = new DataModel();
				}
			}
		}
		return model;
	}
}
