package model.data.process.analysis.constraints;

/**
 * A class for declaring constraints to be placed on rows.
 *
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class Constraint {

    /**
     * The check subclasses should implement
     * @return True if the check passed, false if not
     */
    public abstract boolean check();
}
