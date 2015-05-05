package model.data.process.analysis;

import model.data.DataModel;
import model.data.process.DataProcess;

/**
 * The class to be used for implementing DataAnalysis
 *
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class DataAnalysis extends DataProcess {

    /**
     * The method to be overriden by subclasses to implement the data analysis
     * @param input The input for the analysis
     * @return The output of the analysis
     */
    public abstract DataModel analyse(DataModel input);

    @Override
    protected final DataModel doProcess() {
        return analyse(getInput());
    }
}
