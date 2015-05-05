package model.data.process.analysis.constraints;

import model.data.DataRow;

/**
 * A class for declaring constraints to be placed on rows.
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class Constraint {

	/**
	 * The check subclasses should implement.
	 *
	 * @param row The row the check should be performed on.
	 * @return True if the check passed, false if not
	 */
	public abstract boolean check(DataRow row);
}
