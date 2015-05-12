package model.data;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by jens on 5/12/15.
 *
 * Class that represents a connection between rows.
 */
public class DataConnection {
	private Set<DataRow> causedBy = new HashSet<>();
	private Set<DataRow> resultsIn = new HashSet<>();

	/**
	 * Add a row that is one of the causes of this connection.
	 * @param origin a row that is a cause of this connection
	 */
	public void addCausedBy(DataRow origin) {
		causedBy.add(origin);
		origin.addConnection(this);
	}

	/**
	 * Add a row that is one of the results of this connection.
	 * @param resultIn the row that is a result of this connection.
	 */
	public void addResultsIn(DataRow resultIn) {
		resultsIn.add(resultIn);
		resultIn.addConnection(this);
	}

	/**
	 * Get all the rows that are the cause of this connection.
	 * @return A set of all rows that are the cause of this connection.
	 */
	public Set<DataRow> getCausedBy() {
		return causedBy;
	}

	/**
	 * Get all the rows that are the result of this connection.
	 * @return A set of all rows that are the result of this connection
	 */
	public Set<DataRow> getResultsIn() {
		return resultsIn;
	}
}
