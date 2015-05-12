package model.data.process.analysis;

import model.data.DataTable;
import model.data.process.DataProcess;

/**
 * The class to be used for implementing DataAnalysis
 * <p/>
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class DataAnalysis extends DataProcess {

	/**
	 * The method to be overriden by subclasses to implement the data analysis.
	 *
	 * @param input The input for the analysis
	 * @return The output of the analysis
	 */
	public abstract DataTable analyse(DataTable input);

	@Override
	protected final DataTable doProcess() {
		return analyse(getInput());
	}
}
